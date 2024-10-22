package co.edu.uniquindio.clinicauq.modelo.factory;


import co.edu.uniquindio.clinicauq.modelo.Factura;
import co.edu.uniquindio.clinicauq.modelo.Servicio;

import java.util.List;


public interface Suscripcion {

    List<Servicio> getServiciosDisponibles() throws Exception;
    Factura generarFacturaCobro(Servicio servicio) throws Exception;

    String getTipo();
}
