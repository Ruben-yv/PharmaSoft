package pe.edu.epeu.sysventas.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ProductoResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private Boolean estado;
    private Long categoriaId;
    private String categoriaNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private String creadoPor;
    private String modificadoPor;
}
