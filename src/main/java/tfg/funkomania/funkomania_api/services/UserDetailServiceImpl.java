package tfg.funkomania.funkomania_api.services;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Servicio de detalles de usuario para la autenticación.</p>
 *
 * <p>Implementa la interfaz UserDetailService de Spring Security, permitiendo obtener la información del usuario en la
 * base de datos, sus autoridades (roles) para la autenticación y autorización dentro de la aplicación</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.1.0
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    /** Repositorio de usuarios para acceder a la información de los usuarios en la base de datos. */
    private final IUsuarioRepository usuarioRepository;

    public UserDetailServiceImpl(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtener la información de un usuario base a su nombre de usuario.
     *
     * <p>Marcado como {@code @NullMarked} para indicar que el metodo no acepta valores nulos ni devuelve valores nulos.</p>
     * @param username El nombre de usuario (en este caso, el email) del usuario a buscar.
     * @return Un objeto UserDetails que contiene la información del usuario y sus autoridades (roles).
     * @throws UsernameNotFoundException Si no se encuentra un usuario con el nombre de usuario proporcionado.
     */
    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Obtener el usuario de la base de datos, si no lanzar excepción.
        Usuario usuario = usuarioRepository.findUsuarioByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Usuario con email "+username+" no encontrado en el sistema.")
        );

        // Lista de autoridades individuales del usuario
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Se añade el rol del usuario como autoridad
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));

        // Devolvemos el objeto User de Spring Security con la información del usuario y sus autoridades
        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.isActivo(), // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }
}
