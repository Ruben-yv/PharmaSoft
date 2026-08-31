package pe.edu.epeu.sysventas.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private BigDecimal precio;
    private int stock;
    private Long categoriaId;
    private String categoriaNombre;
}
