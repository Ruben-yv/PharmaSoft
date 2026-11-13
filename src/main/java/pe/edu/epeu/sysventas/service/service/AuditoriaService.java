package pe.edu.epeu.sysventas.service.service;

import java.time.LocalDate;
import pe.edu.epeu.sysventas.auditoria.EventoAuditoria;
import pe.edu.epeu.sysventas.dto.AuditoriaEventoDTO;
import pe.edu.epeu.sysventas.dto.PaginaResponseDTO;

public interface AuditoriaService {
    void registrar(EventoAuditoria evento);

    PaginaResponseDTO<AuditoriaEventoDTO> buscar(
            String usuario,
            String accion,
            LocalDate desde,
            LocalDate hasta,
            int pagina,
            int tamanio);
}
