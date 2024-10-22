package co.edu.uniquindio.clinicauq.modelo.factory;

import co.edu.uniquindio.clinicauq.modelo.Factura;
import co.edu.uniquindio.clinicauq.modelo.Servicio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class  SuscripcionBasica implements Suscripcion {

    private static final double DESCUENTO = 0.10; // 10% de descuento para ciertos servicios de suscripción básica
    @Override
    public List<Servicio> getServiciosDisponibles() {

        List<Servicio> serviciosDisponibles = new ArrayList<>();

        serviciosDisponibles.add(new Servicio(5000, "SB001","Consulta General"));
        serviciosDisponibles.add(new Servicio(4500,"SB002", "Laboratorio"));
        serviciosDisponibles.add(new Servicio(10000, "SB003", "Odontología"));
        serviciosDisponibles.add(new Servicio(11000,"SB004","Oftalmología"));
        serviciosDisponibles.add(new Servicio(13000,"SB005","Cardiología"));

        return serviciosDisponibles;
    }

    @Override
    public Factura generarFacturaCobro(Servicio servicio) {

        double subtotal = servicio.getPrecio();

        // Aplicar descuento si es un servicio con descuento en la suscripción básica
        double total = subtotal * (1 - DESCUENTO);
        String ID = UUID.randomUUID().toString();
        LocalDateTime fecha = LocalDateTime.now();

        return new Factura(fecha, ID, subtotal, total);
    }

    @Override
    public String getTipo() {
        return "Basica";
    }
}
