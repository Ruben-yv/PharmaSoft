package pe.edu.epeu.sysventas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
            max = 50,
            message = "El nombre debe tener entre 3 y 50 caracteres"
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
    private BigDecimal precio;
    @NotNull(message = "El stock es obligatorio")
    private int stock;
    @NotNull(message = "El id de la categoría es obligatorio")
    private Long categoriaId;
}

