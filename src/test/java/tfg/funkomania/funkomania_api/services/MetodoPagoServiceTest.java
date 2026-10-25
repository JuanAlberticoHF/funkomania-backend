package tfg.funkomania.funkomania_api.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import tfg.funkomania.funkomania_api.dtos.metodoPago_dtos.MetodoPagoDTOId;
import tfg.funkomania.funkomania_api.persistence.entities.MetodoPago;
import tfg.funkomania.funkomania_api.persistence.repositories.IMetodoPagoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <p>Clase de prueba unitaria para el servicio {@link MetodoPagoService}.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 1.1.0-SNAPSHOT-1
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de la clase MetodoPagoService")
public class MetodoPagoServiceTest {

    /** Repositorio simulado para la entidad MetodoPago */
    @Mock
    private IMetodoPagoRepository metodoPagoRepository;

    /** Inyección de la implementación del servicio MetodoPagoService */
    @InjectMocks
    private MetodoPagoServiceImpl metodoPagoService;

    /** Configuración inicial antes de cada prueba, si es necesario. */
    @BeforeEach
    void setUp() {
        // Limpiamos el contexto de seguridad antes de cada prueba para evitar interferencias entre pruebas
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Es 1.a: Obtener todos los MetodoPago con 'activo' = true (listado con contenido)")
    void deberiaDevolverUnaListaConLosMetodosDePagoActivos_cuandoExistanDatos() {
        // Given: (Preparación)
        // - Creamos una lista de prueba de métodos de pago activos
        final List<MetodoPago> listaMetodoPago = List.of(
                new MetodoPago(1L, "Tarjeta", true),
                new MetodoPago(2L, "PayPal", true)
        );
        // - Mock configurado para devolver la lista de prueba.
        when(metodoPagoRepository.findMetodoPagosByActivoIsTrue()).thenReturn(listaMetodoPago);

        // When: (Ejecución)
        final List<MetodoPagoDTOId> resultado = metodoPagoService.obtenerMetodosPagoActivos();

        // Then: (Verificación)
        // - Verificar que resultado correcto: no nulo, longitud >= 1 y que los datos coincidan
        assertNotNull(resultado, "El resultado no debe ser nulo");

        assertEquals(listaMetodoPago.size(), resultado.size(), "La cantidad de métodos de pago activos debe coincidir con la lista simulada");

        for (int i = 0; i < listaMetodoPago.size(); i++) {
            assertEquals(listaMetodoPago.get(i).getId(), resultado.get(i).getId(), "El ID del método de pago debe coincidir");
            assertEquals(listaMetodoPago.get(i).getNombre(), resultado.get(i).getNombre(), "El nombre del método de pago debe coincidir");
            assertEquals(listaMetodoPago.get(i).getActivo(), resultado.get(i).getActivo(), "El estado 'activo' del método de pago debe coincidir");
        }

        // - Verificar que el metodo del repositorio fue llamado exactamente una vez
        verify(metodoPagoRepository,times(1)).findMetodoPagosByActivoIsTrue();
    }

    @Test
    @DisplayName("Es 1.b: Obtener todos los MetodoPago con 'activo' = true (listado sin contenido)")
    void deberiaDevolverUnaListaVacia_cuandoNoExistanDatos() {
        // Given: (Preparación)
        // - Creamos una lista de prueba vacía de métodos de pago
        final List<MetodoPago> listaMetodoPago = List.of();
        // - Mock configurado para devolver la lista de prueba.
        when(metodoPagoRepository.findMetodoPagosByActivoIsTrue()).thenReturn(listaMetodoPago);

        // When: (Ejecución)
        final List<MetodoPagoDTOId> resultado = metodoPagoService.obtenerMetodosPagoActivos();

        // Then: (Verificación)
        // - Verificar que resultado correcto: no nulo, longitud >= 1 y que los datos coincidan
        assertNotNull(resultado, "El resultado no debe ser nulo");

        assertEquals(0, resultado.size(), "La cantidad de métodos de pago activos debe ser 0 ya que la lista simulada está vacía");

        // - Verificar que el metodo del repositorio fue llamado exactamente una vez
        verify(metodoPagoRepository,times(1)).findMetodoPagosByActivoIsTrue();
    }

    @Test
    @DisplayName("Es 2: Error base de datos")
    void deberiaLanzarRuntimeException_cuandoOcurreUnErrorEnLaBaseDeDatos() {
        // Given: (Preparación)
        // - Mock configurado para devolver la lista de prueba.
        when(metodoPagoRepository.findMetodoPagosByActivoIsTrue()).thenThrow(new RuntimeException("Error de conexión con la base de datos"));

        // When & // Then: (Ejecución y verificación)

        // - Verificar que se lanza una excepción RuntimeException con el mensaje esperado
        final RuntimeException exception = assertThrows(RuntimeException.class,
                () -> metodoPagoService.obtenerMetodosPagoActivos()
        );

        assertEquals("Error de conexión con la base de datos", exception.getMessage(), "El mensaje de la excepción debe coincidir con el esperado");

        // - Verificar que el metodo del repositorio fue llamado exactamente una vez
        verify(metodoPagoRepository,times(1)).findMetodoPagosByActivoIsTrue();
    }
}
