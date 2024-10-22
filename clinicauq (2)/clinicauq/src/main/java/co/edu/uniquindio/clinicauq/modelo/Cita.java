package co.edu.uniquindio.clinicauq.modelo;

import java.time.LocalDateTime;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class Cita {
    public String ID;
    public LocalDateTime fecha;
    public Paciente paciente;
    public Servicio servicio;
    public String hora;
    public Factura factura;

}

