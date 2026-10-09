package pe.edu.epeu.sysventas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductoRequestDTO {
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(
            min = 3,
            max = 150,
            message = "El nombre debe tener entre 3 y 150 caracteres"
    )
    private String nombre;
    @Size(
            max = 200,
            message = "La descripcion no debe superar los 200 caracteres"
    )
    private String descripcion;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor o igual a 0.01")
    private BigDecimal precio;
    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    private int stock;
    @NotNull(message = "El id de la categoría es obligatorio")
    @Positive(message = "El id de la categoría debe ser positivo")
    private Long categoriaId;
}

