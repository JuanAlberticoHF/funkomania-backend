package tfg.funkomania.funkomania_api.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tfg.funkomania.funkomania_api.dtos.security_dtos.LoginRequest;
import tfg.funkomania.funkomania_api.dtos.security_dtos.TokenResponse;
import tfg.funkomania.funkomania_api.enums.RoleEnum;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioAlreadyExistsException;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;
import tfg.funkomania.funkomania_api.utils.JwtUtils;

import java.time.LocalDateTime;

/**
 * <p>Servicio de autenticación y registro de usuarios.</p>
 * <p>Implementa la lógica de negocio de los métodos definidos en la interfaz AuthService, la inyección de dependencias del IUsuarioRepository y PasswordEncoder.</p>
 *
 * <p>Implementa la anotación @Slf4j para habilitar el registro de eventos y errores en el servicio.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    /** Repositorio de usuarios para acceder a la información de los usuarios en la base de datos. */
    private final IUsuarioRepository IUsuarioRepository;

    /** Codificador de contraseñas para encriptar las contraseñas de los usuarios antes de almacenarlas en la base de datos. */
    private final PasswordEncoder passwordEncoder;

    /** Gestor de autenticación para realizar el proceso de autenticación de los usuarios. */
    private final AuthenticationManager authenticationManager;

    /** Utilidad para generar tokens JWT para la autenticación de los usuarios. */
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(IUsuarioRepository IUsuarioRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtils jwtUtils) {
        this.IUsuarioRepository = IUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Usuario register(Usuario usuario) {
        log.info("Iniciando registro de usuario: {} - {}", usuario.getEmail(), usuario.getNombre());
        // Comprobar si el email ya existe
        if (existsUsuarioByEmail(usuario.getEmail())) {
            throw new UsuarioAlreadyExistsException("El email "+usuario.getEmail()+" ya está registrado en el sistema.");
        }

        // Encriptación de la contraseña
        String passwordHash = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordHash);
        // Asignamos el rol de cliente al usuario
        usuario.setRol(RoleEnum.CLIENTE);
        // Asignamos fecha de registro
        usuario.setFechaRegistro(LocalDateTime.now());
        // Activamos la cuenta
        usuario.setActivo(true);

        // Guardar el usuario en la base de datos
        log.info("Registrando usuario: {} - {}", usuario.getEmail(),  usuario.getNombre());
        return IUsuarioRepository.save(usuario);
    }

    @Override
    public boolean existsUsuarioByEmail(String email) {
        return IUsuarioRepository.existsByEmail(email);
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        log.info("Iniciando login de usuario: {}", loginRequest.username());

        // Autenticación del usuario
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );
        } catch (AuthenticationException e) {
            log.error("Error de autenticación para el usuario: {}", loginRequest.username(), e);
            log.error(e.getMessage());
        }

        log.info("Autenticación exitosa para el usuario: {}", loginRequest.username());

        // Obtener el usuario autenticado
        Usuario usuario = IUsuarioRepository.findUsuarioByEmail(loginRequest.username())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + loginRequest.username()));

        // Generar el token JWT
        String tokenJWT = jwtUtils.generateAccessToken(usuario.getEmail());

        log.info("Token JWT generado para el usuario: {}", loginRequest.username());

        // Devolver el token y los datos del usuario
        return new TokenResponse(tokenJWT, usuario.getEmail(), usuario.getNombre());
    }
}
