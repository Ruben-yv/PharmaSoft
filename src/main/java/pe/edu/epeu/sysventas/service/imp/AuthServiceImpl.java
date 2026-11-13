package pe.edu.epeu.sysventas.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.epeu.sysventas.dto.auth.LoginRequestDTO;
import pe.edu.epeu.sysventas.auditoria.EventoAuditoria;
import pe.edu.epeu.sysventas.dto.auth.LoginResponseDTO;
import pe.edu.epeu.sysventas.dto.auth.CambioPasswordRequestDTO;
import pe.edu.epeu.sysventas.dto.auth.UsuarioResponseDTO;
import pe.edu.epeu.sysventas.entity.Usuario;
import pe.edu.epeu.sysventas.exception.RecursosNoEncontradosException;
import pe.edu.epeu.sysventas.exception.ReglaNegocioException;
import pe.edu.epeu.sysventas.mapper.UsuarioMapper;
import pe.edu.epeu.sysventas.repository.UsuarioRepository;
import pe.edu.epeu.sysventas.security.JwtService;
import pe.edu.epeu.sysventas.security.PermisosPorRol;
import pe.edu.epeu.sysventas.service.service.AuthService;
import pe.edu.epeu.sysventas.service.service.AuditoriaService;

import java.util.List;
import java.nio.charset.StandardCharsets;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log =
            LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            PasswordEncoder passwordEncoder,
            AuditoriaService auditoriaService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {

        String username = request.getUsername().trim();

        log.info("Intento de inicio de sesión: usuario={}", username);

        Authentication autenticacion;
        try {
            autenticacion = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword()));
        } catch (AuthenticationException ex) {
            registrarEventoLogin("LOGIN_FALLIDO", username);
            throw ex;
        }

        UserDetails principal = (UserDetails) autenticacion.getPrincipal();
        Usuario usuario = buscar(principal.getUsername());

        String token = jwtService.generarToken(principal);

        List<String> roles = usuarioMapper.nombresRoles(usuario.getRoles());

        log.info("Inicio de sesión correcto: usuario={} roles={}",
                usuario.getUsername(), roles);
        registrarEventoLogin("LOGIN_EXITOSO", username);

        return new LoginResponseDTO(
                token,
                "Bearer",
                jwtService.getExpiracionSegundos(),
                usuario.getUsername(),
                usuario.getEmpleado().getNombreCompleto(),
                roles,
                PermisosPorRol.permisos(usuario.getRoles())
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO usuarioActual(String username) {
        return usuarioMapper.toUsuarioResponse(buscar(username));
    }

    @Override
    @Transactional
    public void cambiarPassword(String username, CambioPasswordRequestDTO request) {
        Usuario usuario = buscar(username);

        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPassword())) {
            throw new ReglaNegocioException("La contraseña actual no es correcta");
        }
        if (passwordEncoder.matches(request.getPasswordNueva(), usuario.getPassword())) {
            throw new ReglaNegocioException("La nueva contraseña debe ser distinta de la actual");
        }
        if (request.getPasswordNueva().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ReglaNegocioException("La contraseña no puede superar 72 bytes");
        }

        usuario.setPassword(passwordEncoder.encode(request.getPasswordNueva()));
        log.info("Contraseña actualizada para usuario={}", usuario.getUsername());
    }

    private Usuario buscar(String username) {
        return usuarioRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new RecursosNoEncontradosException(
                        "Usuario no encontrado: " + username));
    }

    private void registrarEventoLogin(String accion, String username) {
        try {
            auditoriaService.registrar(EventoAuditoria.login(accion, username));
        } catch (RuntimeException ex) {
            log.error("No se pudo registrar el evento de login {} para usuario={}", accion, username, ex);
        }
    }
}
