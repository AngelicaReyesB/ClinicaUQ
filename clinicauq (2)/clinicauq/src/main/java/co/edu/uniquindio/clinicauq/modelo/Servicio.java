package co.edu.uniquindio.clinicauq.modelo;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Servicio {
    private String ID;
    private String nombre;
    private double precio;

    public Servicio(double i, String s001, String consultaGeneral) {
    }

}

