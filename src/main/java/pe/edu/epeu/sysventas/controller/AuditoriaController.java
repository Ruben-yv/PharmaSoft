package pe.edu.epeu.sysventas.controller;

import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.epeu.sysventas.dto.AuditoriaEventoDTO;
import pe.edu.epeu.sysventas.dto.PaginaResponseDTO;
import pe.edu.epeu.sysventas.service.service.AuditoriaService;

@RestController
@Validated
@RequestMapping("/api/v1/auditoria")
public class AuditoriaController {
    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public ResponseEntity<PaginaResponseDTO<AuditoriaEventoDTO>> buscar(
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String accion,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "0") @Min(0) int pagina,
            @RequestParam(defaultValue = "20") @Min(1) int tamanio) {
        return ResponseEntity.ok(auditoriaService.buscar(usuario, accion, desde, hasta, pagina, tamanio));
    }
}
