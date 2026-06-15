package tfg.funkomania.funkomania_api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.repositories.IProductoRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListaDeseosServiceImplTest {

    @Mock
    private IUsuarioRepository usuarioRepository;
    @Mock
    private IProductoRepository productoRepository;

    @InjectMocks
    private ListaDeseosServiceImpl listaDeseosService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testObtenerListaDeseosDelUsuario_UsuarioNoEncontrado() {
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("test@test.com");
        when(usuarioRepository.findUsuarioByEmail("test@test.com")).thenReturn(Optional.of(new Usuario()));
        when(usuarioRepository.findUsuarioByIdConListaDeseos(null)).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class,
                () -> listaDeseosService.obtenerListaDeseosDelUsuario());
    }
}
