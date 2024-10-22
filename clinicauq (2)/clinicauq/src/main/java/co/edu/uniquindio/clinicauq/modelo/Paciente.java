package co.edu.uniquindio.clinicauq.modelo;
import co.edu.uniquindio.clinicauq.modelo.factory.Suscripcion;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Data

public class Paciente {

    String cedula;
    String nombre;
    String telefono;
    String correo;
    Suscripcion suscripcion;


}
