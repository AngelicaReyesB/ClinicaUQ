package co.edu.uniquindio.clinicauq.modelo;
import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString

public class Factura {
    LocalDateTime fecha;
    String codigo;
    double total;
    double subtotal;

}
