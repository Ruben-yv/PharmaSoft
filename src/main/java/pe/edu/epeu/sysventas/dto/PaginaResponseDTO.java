package pe.edu.epeu.sysventas.dto;

import java.util.List;
import java.io.Serializable;

public record PaginaResponseDTO<T>(
        List<T> contenido,
        int pagina,
        int tamanio,
        long totalElementos,
        int totalPaginas,
        boolean ultima) implements Serializable {
}
