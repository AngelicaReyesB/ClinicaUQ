package co.edu.uniquindio.clinicauq.modelo;

import co.edu.uniquindio.clinicauq.modelo.factory.Suscripcion;
import co.edu.uniquindio.clinicauq.utils.EnvioEmail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@ToString
@Setter
@Getter
public class Clinica {

    public List<Cita> citas;
    public List<Servicio> servicios;
    public List<Paciente> pacientes;
    public List<Factura> facturas;

    private static Clinica INSTANCIA;

    public Clinica() {
        this.citas = new ArrayList<>();
        this.servicios = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.facturas = new ArrayList<>();
        crearServicios();
    }

    public static Clinica getInstance() {
        if (INSTANCIA == null) {
            INSTANCIA = new Clinica();
        }
            return INSTANCIA;

    }

    private void crearServicios() {
        servicios.add(new Servicio(10000, "S001", "Consulta General"));
        servicios.add(new Servicio(9500, "S002", "Laboratorio"));
        servicios.add(new Servicio(15000, "S003", "Odontología"));
        servicios.add(new Servicio(17000, "S004", "Oftalmología"));
        servicios.add(new Servicio(20000, "S005", "Cardiología"));
        servicios.add(new Servicio(15000,"S006", "Citología"));
        servicios.add(new Servicio(20000,"S005","Psicología"));
    }

    public void registrarPaciente(String cedula, String nombre, String telefono, String correo, Suscripcion suscripcion) throws Exception {
        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty() || suscripcion == null) {
            throw new Exception("Todos los campos son obligatorios.");
        }

        if (!esTelefonoValido(telefono)) {
            throw new IllegalArgumentException("El teléfono debe contener 10 dígitos.");
        }
        if (!esCorreoValido(correo)) {
            throw new IllegalArgumentException("El correo no es válido.");
        }

        for (Paciente paciente : pacientes) {
            if (paciente.getCedula().equals(cedula)) {
                throw new Exception("El paciente con cédula " + cedula + " ya está registrado.");
            }
        }

        Paciente paciente = Paciente.builder()
                .cedula(cedula)
                .nombre(nombre)
                .telefono(telefono)
                .correo(correo)
                .suscripcion(suscripcion)
                .build();

        pacientes.add(paciente);
    }

    public static boolean esCorreoValido(String correo) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return correo != null && Pattern.compile(emailRegex).matcher(correo).matches();
    }

    // Método para validar el número de teléfono
    private boolean esTelefonoValido(String numeroTelefono) {
        return numeroTelefono.matches("\\d{10}");
    }

    public void registrarCita(Cita cita) throws Exception {
        if (cita.getPaciente() == null || cita.getID() == null || cita.getFecha() == null || cita.getServicio() == null || cita.getFactura() == null) {
            throw new Exception("No se pudo registrar la cita.");
        }

        if (existeCita(cita)) {
            throw new Exception("El paciente ya tiene una cita agendada a la misma hora.");
        }

        if (!existePaciente(cita.getPaciente().getCedula())) {
            throw new IllegalArgumentException("El paciente no está registrado.");
        }

        if (!servicios.stream().anyMatch(s -> s.getID().equals(cita.getServicio().getID()))) {
            throw new IllegalArgumentException("El servicio no está disponible.");
        }

        Cita c = Cita.builder()
                .paciente(cita.paciente)
                .ID(cita.ID)
                .fecha(cita.fecha)
                .servicio(cita.servicio)
                .factura(cita.factura)
                .build();

        citas.add(c);

        // Enviar notificación por email al paciente
        String destinatario = cita.paciente.getCorreo();
        String asunto = "Confirmación de Cita";
        String mensaje = "Estimado/a " + cita.paciente.getNombre() + ",\n\n" +
                "Su cita ha sido agendada exitosamente.\n" +
                "Detalles de la cita:\n" +
                "Servicio: " + cita.servicio.getNombre() + "\n" +
                "Fecha: " + cita.fecha.toString() + "\n" +
                "Total: " + cita.factura.getTotal() + "\n\n" +
                "Gracias por elegir nuestra clínica.\n" +
                "Saludos,\n";

        EnvioEmail.enviarNotificacion(destinatario, asunto, mensaje);
    }


    // Método que verifica si ya existe un paciente con la cédula dada
    private boolean existePaciente(String cedula) {
        return pacientes.stream().anyMatch(paciente -> paciente.getCedula().equalsIgnoreCase(cedula));
    }

    // Método que verifica si ya existe una Cita
    private boolean existeCita(Cita cita) {
        for (Cita c : citas) {
            if (cita.getPaciente().equals(cita.paciente)
                    && cita.getID().equals(cita.ID)
                    && cita.getFecha().equals(cita.fecha)) {
                return  true;
            }
        }
        return false;
    }

    public List<Servicio> getServiciosDisponibles(Suscripcion suscripcion) throws Exception {
        List<Servicio> serviciosDisponibles;

        if (suscripcion != null) {
            serviciosDisponibles = suscripcion.getServiciosDisponibles();

        } else {
            // Si no hay suscripción, se devuelven todos los servicios de la clínica
            serviciosDisponibles = new ArrayList<>();
            serviciosDisponibles.add(new Servicio(10000, "S001", "Consulta General"));
            serviciosDisponibles.add(new Servicio(9500, "S002", "Laboratorio"));
            serviciosDisponibles.add(new Servicio(15000, "S003", "Odontología"));
            serviciosDisponibles.add(new Servicio(17000, "S004", "Oftalmología"));
            serviciosDisponibles.add(new Servicio(20000, "S005", "Cardiología"));
            serviciosDisponibles.add(new Servicio(15000, "S006", "Citología"));
            serviciosDisponibles.add(new Servicio(20000, "S007", "Psicología"));
        }

        if (serviciosDisponibles.isEmpty()) {
            throw new Exception("No hay servicios disponibles.");
        }

        return serviciosDisponibles;
    }

    public void cancelarCita(Cita cita) {
        citas.remove(cita);
    }

    public Paciente obtenerPacientePorCedula(String cedula) {
        if (cedula == null || cedula.isEmpty()) {
            return null;
        }


        for (Paciente paciente : pacientes) {
            if (paciente.getCedula().equals(cedula)) {
                return paciente;
            }
        }

        return null;
    }

    public List<Paciente> listarPacientes() {
        return pacientes;
    }

    public List<Cita> listarCitas() {
        return citas;
    }
    //-----------------------------------------------------------------------------------------------------//
    public void crearServicios(Servicio servicio) throws Exception {
        if (servicio.getNombre() == null || servicio.getNombre().isEmpty()) {
            throw new Exception("El nombre del servicio a tomar es obligatorio.");
        }

        // Verificar si ya existe un servicio con el mismo código
        if (servicios.stream().anyMatch(s -> s.getID().equalsIgnoreCase(servicio.getID()))) {
            throw new IllegalArgumentException("Ya existe un servicio con el código ingresado.");
        }

        Servicio s = Servicio.builder()
                .ID(servicio.getID())
                .nombre(servicio.getNombre())
                .build();

        servicios.add(s);
    }

    public Factura generarFactura(Paciente paciente, Servicio servicio) throws Exception {
        if (paciente == null || servicio == null) {
            throw new Exception("El paciente y el servicio no pueden ser nulos.");
        }

        return paciente.getSuscripcion().generarFacturaCobro(servicio);
    }

    public Factura crearFactura(LocalDateTime fecha, String codigo, double subtotal, double total) throws Exception {
        Factura factura= Factura.builder()
                .codigo(codigo)
                .subtotal(Double.parseDouble(String.valueOf(subtotal)))
                .total(Double.parseDouble(String.valueOf(total)))
                .build();
        facturas.add(factura);
        return factura;
    }


}