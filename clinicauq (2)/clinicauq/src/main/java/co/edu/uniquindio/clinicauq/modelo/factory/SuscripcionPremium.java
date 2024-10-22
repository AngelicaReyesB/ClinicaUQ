package co.edu.uniquindio.clinicauq.modelo.factory;
import co.edu.uniquindio.clinicauq.modelo.Factura;
import co.edu.uniquindio.clinicauq.modelo.Servicio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Builder
public class SuscripcionPremium implements Suscripcion{

    Factura factura;
    List<Servicio> servicios;
    private static final double DESCUENTO = 0.20; // 20% de descuento para servicios de suscripción premium

    public SuscripcionPremium() {

    }


    @Override
    public List<Servicio> getServiciosDisponibles() {

        List<Servicio> serviciosDisponibles = new ArrayList<>();

        serviciosDisponibles.add(new Servicio(0, "SP001","Consulta General"));
        serviciosDisponibles.add(new Servicio(0,"SP002", "Laboratorio"));
        serviciosDisponibles.add(new Servicio(5900, "SP003", "Odontología"));
        serviciosDisponibles.add(new Servicio(7000,"SP004","Oftalmología"));
        serviciosDisponibles.add(new Servicio(8500,"SP005","Cardiología"));
        serviciosDisponibles.add(new Servicio(9500,"SP006", "Citología"));
        serviciosDisponibles.add(new Servicio(10500,"SP007","Psicología"));

        return serviciosDisponibles;
    }

    @Override
    public Factura generarFacturaCobro(Servicio servicio) {

        double subtotal = servicio.getPrecio();

        // Aplicar descuento si es un servicio con descuento en la suscripción premium
        double total = subtotal * (1 - DESCUENTO);
        String ID = UUID.randomUUID().toString();
        LocalDateTime fecha = LocalDateTime.now();

        return new Factura(fecha, ID, subtotal, total);
    }

    @Override
        public String getTipo() {
        return "Premium";
    }

}
