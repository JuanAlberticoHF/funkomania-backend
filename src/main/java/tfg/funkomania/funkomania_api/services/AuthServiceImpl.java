package tfg.funkomania.funkomania_api.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tfg.funkomania.funkomania_api.enums.RoleEnum;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioAlreadyExistsException;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;

import java.time.LocalDateTime;

/**
 * <p>Servicio de autenticación y registro de usuarios.</p>
 * <p>Implementa la lógica de negocio de los métodos definidos en la interfaz AuthService, la inyección de dependencias del IUsuarioRepository y PasswordEncoder.</p>
 *
 * @author JuanAlbeticoHF
 * @version 0.1.2
 * @since 0.1.0
 */
@Service
public class AuthServiceImpl implements AuthService{

    private final IUsuarioRepository IUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(IUsuarioRepository IUsuarioRepository, PasswordEncoder passwordEncoder) {
        this.IUsuarioRepository = IUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario registerUsuario(Usuario usuario) {
    public Usuario register(Usuario usuario) {
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
        return IUsuarioRepository.save(usuario);
    }

    @Override
    public boolean existsUsuarioByEmail(String email) {
        return IUsuarioRepository.existsByEmail(email);
    }
}
