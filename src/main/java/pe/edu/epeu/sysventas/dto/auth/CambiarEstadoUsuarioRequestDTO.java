package pe.edu.epeu.sysventas.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CambiarEstadoUsuarioRequestDTO {

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}
