package pe.edu.epeu.sysventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean estado;
    private BigDecimal precio;
    private int sotck;
}
