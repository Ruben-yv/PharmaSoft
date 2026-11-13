package pe.edu.epeu.sysventas.service.imp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.epeu.sysventas.auditoria.EventoAuditoria;
import pe.edu.epeu.sysventas.dto.AuditoriaEventoDTO;
import pe.edu.epeu.sysventas.dto.PaginaResponseDTO;
import pe.edu.epeu.sysventas.entity.AuditoriaEvento;
import pe.edu.epeu.sysventas.exception.ReglaNegocioException;
import pe.edu.epeu.sysventas.repository.AuditoriaEventoRepository;
import pe.edu.epeu.sysventas.service.service.AuditoriaService;
import pe.edu.epeu.sysventas.util.PaginacionUtil;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {
    private static final Logger log = LoggerFactory.getLogger(AuditoriaServiceImpl.class);
    private static final Set<String> CAMPOS_ORDENABLES = Set.of("fecha", "usuario", "accion");

    private final AuditoriaEventoRepository repository;

    public AuditoriaServiceImpl(AuditoriaEventoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(EventoAuditoria evento) {
        AuditoriaEvento auditoria = new AuditoriaEvento();
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setUsuario(recortar(evento.usuario(), 50));
        auditoria.setAccion(recortar(evento.accion(), 40));
        auditoria.setEntidad(recortar(evento.entidad(), 40));
        auditoria.setEntidadId(evento.entidadId());
        auditoria.setDetalle(recortar(evento.detalle(), 500));
        auditoria.setRequestId(recortar(evento.requestId(), 36));
        repository.save(auditoria);
        log.info("Auditoría {} | {} {} | usuario={} | requestId={}", auditoria.getAccion(),
                auditoria.getEntidad(), auditoria.getEntidadId(), auditoria.getUsuario(), auditoria.getRequestId());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginaResponseDTO<AuditoriaEventoDTO> buscar(
            String usuario,
            String accion,
            LocalDate desde,
            LocalDate hasta,
            int pagina,
            int tamanio) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new ReglaNegocioException("La fecha desde no puede ser posterior a la fecha hasta");
        }
        Pageable pageable = PaginacionUtil.construir(
                pagina, tamanio, "fecha", "desc", CAMPOS_ORDENABLES, "fecha");
        LocalDateTime desdeHora = desde == null ? null : desde.atStartOfDay();
        LocalDateTime hastaExclusiva = hasta == null ? null : hasta.plusDays(1).atStartOfDay();
        return PaginaResponseDTO.de(repository.buscar(
                vacioANulo(usuario), vacioANulo(accion), desdeHora, hastaExclusiva, pageable)
                .map(AuditoriaEventoDTO::de));
    }

    private static String vacioANulo(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private static String recortar(String valor, int maximo) {
        if (valor == null || valor.length() <= maximo) return valor;
        return valor.substring(0, maximo);
    }
}
