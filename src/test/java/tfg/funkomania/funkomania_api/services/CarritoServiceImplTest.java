package tfg.funkomania.funkomania_api.services;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.repositories.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CarritoServiceImplTest {

    @Mock
    private ICarritoRepository carritoRepository;
    @Mock
    private IDetalleCarritoRepository detalleCarritoRepository;
    @Mock
    private IVistaCarritoContenidoRepository vistaCarritoContenidoRepository;
    @Mock
    private IVistaCarritoTotalesRepository vistaCarritoTotalesRepository;
    @Mock
    private IUsuarioRepository usuarioRepository;
    @Mock
    private IProductoRepository productoRepository;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testObtenerCarritoCompletoUsuario_UsuarioNoEncontrado() {
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("test@test.com");
        when(usuarioRepository.findUsuarioByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThrows(tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioNotFoundException.class,
                () -> carritoService.obtenerCarritoCompletoUsuario());
    }
}
