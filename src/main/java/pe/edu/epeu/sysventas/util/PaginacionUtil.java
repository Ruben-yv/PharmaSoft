package pe.edu.epeu.sysventas.util;

import java.util.Set;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pe.edu.epeu.sysventas.exception.ReglaNegocioException;

public final class PaginacionUtil {
    private PaginacionUtil() { }

    public static Pageable construir(
            int pagina,
            int tamanio,
            String ordenarPor,
            String direccion,
            Set<String> camposOrdenables,
            String campoPorDefecto) {
        if (pagina < 0) {
            throw new ReglaNegocioException("La página debe ser mayor o igual a cero");
        }
        if (tamanio < 1 || tamanio > 100) {
            throw new ReglaNegocioException("El tamaño debe estar entre 1 y 100");
        }

        String campo = ordenarPor == null || ordenarPor.isBlank() ? campoPorDefecto : ordenarPor.trim();
        if (!camposOrdenables.contains(campo)) {
            throw new ReglaNegocioException("Campo de orden no válido. Use: " + camposOrdenables);
        }

        Sort.Direction sentido;
        try {
            sentido = Sort.Direction.fromString(direccion == null || direccion.isBlank()
                    ? "desc" : direccion.trim());
        } catch (IllegalArgumentException ex) {
            throw new ReglaNegocioException("Dirección no válida. Use asc o desc");
        }
        return PageRequest.of(pagina, tamanio, Sort.by(sentido, campo));
    }
}
