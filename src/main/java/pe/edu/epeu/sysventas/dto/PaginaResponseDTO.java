package pe.edu.epeu.sysventas.dto;

import java.util.List;
import java.io.Serializable;
import org.springframework.data.domain.Page;

public record PaginaResponseDTO<T>(
        List<T> contenido,
        int pagina,
        int tamanio,
        long totalElementos,
        int totalPaginas,
        boolean ultima) implements Serializable {

    public static <T> PaginaResponseDTO<T> de(Page<T> pagina) {
        return new PaginaResponseDTO<>(pagina.getContent(), pagina.getNumber(), pagina.getSize(),
                pagina.getTotalElements(), pagina.getTotalPages(), pagina.isLast());
    }
}
