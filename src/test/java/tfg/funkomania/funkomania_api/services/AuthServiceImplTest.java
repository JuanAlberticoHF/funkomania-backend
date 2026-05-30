package tfg.funkomania.funkomania_api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import tfg.funkomania.funkomania_api.enums.RoleEnum;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioAlreadyExistsException;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias para la implementación del servicio de autenticación.
 *
 * <p>Se valida el comportamiento del registro de usuarios y la comprobación
 * de existencia por email mediante mocks de repositorio y codificador de contraseña.</p>
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    /**
     * Debe encriptar la contraseña y asignar los valores por defecto al registrar un usuario.
     */
    @Test
    void registrarUsuario_deberiaEncriptarContrasenaYAsignarValoresPorDefecto() {
        Usuario usuario = new Usuario();
        usuario.setEmail("user@example.com");
        usuario.setPassword("plain-pass");
        usuario.setNombre("Nombre");

        when(usuarioRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plain-pass")).thenReturn("hashed-pass");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario result = authService.registerUsuario(usuario);

        assertThat(result.getPassword()).isEqualTo("hashed-pass");
        assertThat(result.getRol()).isEqualTo(RoleEnum.CLIENTE);
        assertThat(result.isActivo()).isTrue();
        assertThat(result.getFechaRegistro()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now());
        verify(usuarioRepository).save(usuario);
    }

    /**
     * Debe lanzar una excepción cuando ya existe un usuario con el mismo email.
     */
    @Test
    void registrarUsuario_deberiaLanzarExcepcionCuandoEmailExiste() {
        Usuario usuario = new Usuario();
        usuario.setEmail("user@example.com");

        when(usuarioRepository.existsByEmail("user@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.registerUsuario(usuario))
                .isInstanceOf(UsuarioAlreadyExistsException.class);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    /**
     * Debe delegar en el repositorio la comprobación de existencia por email.
     */
    @Test
    void existeUsuarioPorEmail_deberiaDelegarEnRepositorio() {
        when(usuarioRepository.existsByEmail("user@example.com")).thenReturn(true);

        boolean exists = authService.existsUsuarioByEmail("user@example.com");

        assertThat(exists).isTrue();
        verify(usuarioRepository).existsByEmail("user@example.com");
    }
}

