package tfg.funkomania.funkomania_api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import tfg.funkomania.funkomania_api.persistence.entities.VistaAdminAlertasStock;
import tfg.funkomania.funkomania_api.persistence.entities.VistaAdminAlertasStockDTOId;
import tfg.funkomania.funkomania_api.persistence.repositories.IVistaAdminAlertasStockRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <p>Clase de prueba unitaria para el servicio {@link AlertasStockService}.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 1.1.0-SNAPSHOT-2
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de la clase AlertasStockService")
public class AlertasStockServiceTest {

    /** Repositorio simulado para la entidad VistaAdminAlertasStock */
    @Mock
    private IVistaAdminAlertasStockRepository vistaAdminAlertasStockRepository;

    /** Inyección de la implementación del servicio AlertasStockService */
    @InjectMocks
    private AlertasStockServiceImpl alertasStockService;

    /** Configuración inicial antes de cada prueba, si es necesario. */
    @BeforeEach
    void setUp() {
        // Limpiamos el contexto de seguridad antes de cada prueba para evitar interferencias entre pruebas
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Es 1.a: Obtener todos las alertas de stock (con contenido)")
    void deberiaDevolverUnaListaConLasAlertasDeStock_cuandoExistanDatos() {
        // Given: (Preparación)
        // - Creamos una lista de prueba de alertas de stock activas
        List<VistaAdminAlertasStock> listaAlertasStock = List.of(
                new VistaAdminAlertasStock(1L, "Producto 1", 0, 1L, "Agotado"),
                new VistaAdminAlertasStock(2L, "Producto 2", 3, 2L, "Critico"),
                new VistaAdminAlertasStock(3L, "Producto 3", 6, 3L, "Bajo")
        );

        // - Mock configurado para devolver la lista de prueba.
        when(vistaAdminAlertasStockRepository.findAll()).thenReturn(listaAlertasStock);

        // When: (Ejecución)
        // - Llamamos al metodo del servicio para obtener las alertas de stock activas
        List<VistaAdminAlertasStockDTOId> resultado = alertasStockService.obtenerAlertasStock();

        // Then: (Verificación)
        // - Verificar que resultado correcto: no nulo, longitud >= 1 y que los datos coincidan
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(listaAlertasStock.size(), resultado.size(), "La longitud de la lista debería coincidir con la lista de prueba");

        for (int i = 0; i < listaAlertasStock.size(); i++) {
            assertEquals(listaAlertasStock.get(i).getIdProducto(), resultado.get(i).getIdProducto(), "El ID de la alerta de stock debe coincidir");
            assertEquals(listaAlertasStock.get(i).getNombre(), resultado.get(i).getNombre(), "El nombre de la alerta de stock debe coincidir");
            assertEquals(listaAlertasStock.get(i).getStock(), resultado.get(i).getStock(), "El stock de la alerta de stock debe coincidir");
            assertEquals(listaAlertasStock.get(i).getIdCategoria(), resultado.get(i).getIdCategoria(), "El ID de la categoría de la alerta de stock debe coincidir");
            assertEquals(listaAlertasStock.get(i).getPrioridad(), resultado.get(i).getPrioridad(), "El nivel de prioridad de la alerta de stock debe coincidir");
        }

        // - Verificar que el metodo del repositorio fue llamado exactamente una vez
        verify(vistaAdminAlertasStockRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Es 1.b: Obtener todos las alertas de stock (sin contenido)")
    void deberiaDevolverUnaListaVaciaSinAlertasDeStock_cuandoNoExistanDatos() {
        // Given: (Preparación)
        // - Mock configurado para devolver la lista vacía.
        when(vistaAdminAlertasStockRepository.findAll()).thenReturn(List.of());

        // When: (Ejecución)
        // - Llamamos al metodo del servicio para obtener las alertas de stock activas
        List<VistaAdminAlertasStockDTOId> resultado = alertasStockService.obtenerAlertasStock();

        // Then: (Verificación)
        // - Verificar que resultado correcto: no nulo, longitud >= 1 y que los datos coincidan
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(0, resultado.size(), "La longitud de la lista debería ser 0 ya que no hay alertas de stock");

        // - Verificar que el metodo del repositorio fue llamado exactamente una vez
        verify(vistaAdminAlertasStockRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Es 2: Error base de datos")
    void deberiaLanzarRuntimeException_cuandoOcurreUnErrorEnLaBaseDeDatos() {
        // Given: (Preparación)
        // - Mock configurado para lanzar excepción
        when(vistaAdminAlertasStockRepository.findAll()).thenThrow(new RuntimeException("Error de conexión con la base de datos"));

        // When & // Then: (Ejecución y verificación)

        // - Verificar que se lanza una excepción RuntimeException con el mensaje esperado
        final RuntimeException exception = assertThrows(RuntimeException.class,
                () -> vistaAdminAlertasStockRepository.findAll()
        );

        assertEquals("Error de conexión con la base de datos", exception.getMessage(), "El mensaje de la excepción debe coincidir con el esperado");

        // - Verificar que el metodo del repositorio fue llamado exactamente una vez
        verify(vistaAdminAlertasStockRepository,times(1)).findAll();
    }
}
