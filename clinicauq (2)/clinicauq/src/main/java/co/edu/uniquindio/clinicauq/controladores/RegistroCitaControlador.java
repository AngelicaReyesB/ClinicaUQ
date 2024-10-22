package co.edu.uniquindio.clinicauq.controladores;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

import co.edu.uniquindio.clinicauq.modelo.Cita;
import co.edu.uniquindio.clinicauq.modelo.Clinica;
import co.edu.uniquindio.clinicauq.modelo.Paciente;
import co.edu.uniquindio.clinicauq.modelo.Servicio;
import co.edu.uniquindio.clinicauq.modelo.factory.Suscripcion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistroCitaControlador {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private DatePicker fechaDate;
    @FXML private Button btnconfirmarCita;
    @FXML private ComboBox<Servicio> comboServicio;
    @FXML private TextField horatxt;
    @FXML private TextField pacientetxt;
    private final Clinica clinica;
    private Paciente paciente;
    // Constructor que obtiene la instancia de Clinica
    public RegistroCitaControlador() {
        clinica = Clinica.getInstance();
    }

    private void cargarServiciosDisponibles() {
        try {
            String pacienteID = pacientetxt.getText();
            if (pacienteID == null || pacienteID.isEmpty()) {
                mostrarAlerta("Error", "Debe ingresar la cédula del paciente.", Alert.AlertType.ERROR);
                return;
            }

            Paciente paciente = clinica.obtenerPacientePorCedula(pacienteID);
            if (paciente != null) {
                Suscripcion suscripcion = paciente.getSuscripcion();
                List<Servicio> serviciosDisponibles = suscripcion.getServiciosDisponibles();
                ObservableList<Servicio> serviciosObservable = FXCollections.observableArrayList(serviciosDisponibles);
                comboServicio.setItems(serviciosObservable);
            } else {
                mostrarAlerta("Error", "El paciente no está registrado.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron cargar los servicios: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    void initialize() {
        assert fechaDate != null : "fx:id=\"fechaDate\" was not injected: check your FXML file 'registroCita.fxml'.";
        assert btnconfirmarCita != null : "fx:id=\"btnconfirmarCita\" was not injected: check your FXML file 'registroCita.fxml'.";
        assert comboServicio != null : "fx:id=\"comboServicio\" was not injected: check your FXML file 'registroCita.fxml'.";
        assert horatxt != null : "fx:id=\"horatxt\" was not injected: check your FXML file 'registroCita.fxml'.";
        assert pacientetxt != null : "fx:id=\"pacientetxt\" was not injected: check your FXML file 'registroCita.fxml'.";
        cargarServiciosDisponibles();
    }

    //Metodo de registar Cita del boton registrar
    @FXML
    void registrarCita(ActionEvent event) {
        try {

            String pacienteID = pacientetxt.getText();
            LocalDate fechaCita = fechaDate.getValue();
            LocalTime horaCita = LocalTime.parse(horatxt.getText());
            Servicio servicioSeleccionado = comboServicio.getSelectionModel().getSelectedItem();
            // Valida campos vacíos
            if (pacienteID.isEmpty() || fechaCita == null || horaCita == null || servicioSeleccionado == null) {
                mostrarAlerta("Error", "Todos los campos deben estar llenos.", Alert.AlertType.ERROR);
                return;
            }

            // Crea una instancia de Paciente
            Paciente paciente = clinica.obtenerPacientePorCedula(pacienteID);
            if (paciente == null) {
                mostrarAlerta("Error", "El paciente no está registrado.", Alert.AlertType.ERROR);
                return;
            }

            // Obtiene la suscripción del paciente
            Suscripcion suscripcion = paciente.getSuscripcion();
            List<Servicio> serviciosDisponibles = clinica.getServiciosDisponibles(suscripcion);

            // Verifica si el servicio seleccionado está disponible
            Servicio servicio = null;
            for (Servicio s : serviciosDisponibles) {
                if (s.getNombre().equals(servicioSeleccionado)) {
                    servicio = s;
                    break;
                }
            }

            if (servicio == null) {
                mostrarAlerta("Error", "El servicio no está disponible para su suscripción.", Alert.AlertType.ERROR);
                return;
            }

            // Crea la Cita
            Cita nuevaCita = Cita.builder()
                    .paciente(paciente)
                    .ID("ID_AUTOGENERADO")
                    .fecha(fechaCita.atTime(horaCita))
                    .servicio(servicioSeleccionado)
                    .factura(null)
                    .build();


            clinica.registrarCita(nuevaCita);
            cargarServiciosDisponibles();

            // Mostrar confirmación
            mostrarAlerta("Éxito", "La cita se ha registrado exitosamente.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    /**
     * Método que se encarga de enviarcorreo
    private void enviarCorreo(String idPersona, String codigo,String mensaje) throws Exception {
        System.out.println("ESTO ES LO QUE RECIBE PARA BUSCAR"+ idPersona);
        Paciente paciente = panelControlador.obtenerPersonas(idPersona);
        if (paciente != null) {
            String email = paciente.getCorreo();
            if (email != null && !email.isBlank()) {
                EnvioEmail envioEmail = new EnvioEmail();
                // Podrías considerar pasar las credenciales de correo electrónico como argumentos o leerlas de alguna configuración
                envioEmail.destinatario = email;
                envioEmail.asunto = "Notificación de entrega de paquete";
                envioEmail.mensaje = mensaje;

                // Intentar enviar el correo electrónico
                try {
                    envioEmail.enviarNotificacion();
                } catch (Exception e) {
                    controladorPrincipal.mostrarAlerta("Error al enviar el correo electrónico: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                controladorPrincipal.mostrarAlerta("El correo electrónico del usuario no está disponible.", Alert.AlertType.WARNING);
            }
        } else {
            controladorPrincipal.mostrarAlerta("No se pudo encontrar la persona con el ID proporcionado.", Alert.AlertType.WARNING);
}
}

     public void facturar(ActionEvent actionEvent) throws Exception {
     enviar.setDisable(false);
     String tipos = (String) selectCategorys.getValue();
     TipoEnvio tipo = TipoEnvio.valueOf(tipos);
     String codigo = controladorPrincipal.generarCodigo(tipo);
     String valor =labelValor.getText();
     String subTotal = String.valueOf(controladorPrincipal.calcularPericoSubTotal(distancia, tipo, pesoTotal, cantidadPaquetes));
     controladorPrincipal.crearFactura(codigo,valor,subTotal);
     facturas.setText("Su factura con la empresa Enviaos por la cantidad de " + cantidadPaquetes + " paquetes \nes de un costo de:\nSubTotal: " + subTotal + "\nTotal: " + valor);
     }
     }*/

}