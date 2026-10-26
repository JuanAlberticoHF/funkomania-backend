package tfg.funkomania.funkomania_api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import tfg.funkomania.funkomania_api.dtos.direccion_dtos.DireccionDTOId;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioNotFoundException;
import tfg.funkomania.funkomania_api.persistence.entities.Direccion;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.repositories.IDireccionRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IUsuarioRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * <p>Clase de prueba unitaria para el servicio {@link DireccionAdminService}.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 1.1.0-SNAPSHOT-2
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias del servicio DireccionService (solo DireccionAdminService)")
public class DireccionAdminServiceTest {

    /** Repositorio simulado para la entidad Direccion */
    @Mock
    private IDireccionRepository direccionRepository;

    /** Repositorio simulado para la entidad Usuario */
    @Mock
    private IUsuarioRepository usuarioRepository;

    /** Inyección de la implementación del servicio DireccionService */
    @InjectMocks
    private DireccionServiceImpl direccionAdminServiceImpl;

    /** Configuración inicial antes de cada prueba, si es necesario. */
    @BeforeEach
    void setUp() {
        // Limpiamos el contexto de seguridad antes de cada prueba para evitar interferencias entre pruebas
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Es 1.a: Obtener todas las direcciones de un usuario (con contenido)")
    void deberiaDevolverUnaListaDeDireccionesDeUnUsuario_conContenido() {
        // Given: (Preparación)
        // - Creamos un identificador de usuario de prueba
        final Long idUsuario = 1L;

        // - Creamos una lista de direcciones de prueba para el usuario
        final List<Direccion> listaDirecciones = List.of(
                new Direccion(1L, new Usuario(), "Calle Falsa", "123", "2", "B", "Ciudad A", "Municipio A", "Provincia A", "12345", true),
                new Direccion(2L, new Usuario(), "Avenida Siempre Viva", "456", "3", "C", "Ciudad B", "Municipio B", "Provincia B", "67890", true),
                new Direccion(3L, new Usuario(), "Calle Real", "789", null, null, "Ciudad C", "Municipio C", "Provincia C", "54321", true)
        );

        // - Mock configurado para simular que el usuario existe en la base de datos.
        when(usuarioRepository.existsById(idUsuario)).thenReturn(true);

        // - Mock configurado para devolver una lista de direcciones de prueba.
        when(direccionRepository.findDireccionsByUsuario_IdUsuario(idUsuario)).thenReturn(listaDirecciones);

        // When: (Ejecución)
        List<DireccionDTOId> resultado = direccionAdminServiceImpl.getDireccionesByUsuarioId(idUsuario);

        // Then: (Verificación)
        // - Verificar que el listado no sea nulo, que la longitud sea correcta y que los datos sean los esperados
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(listaDirecciones.size(), resultado.size(), "La lista de direcciones debería contener "+listaDirecciones.size()+" elementos.");

        for (int i = 0; i < listaDirecciones.size(); i++) {
            assertEquals(listaDirecciones.get(i).getId(), resultado.get(i).getId(), "El ID de la dirección debe coincidir");
            assertEquals(listaDirecciones.get(i).getCalle(), resultado.get(i).getCalle(), "La calle de la dirección debe coincidir");
            assertEquals(listaDirecciones.get(i).getNumero(), resultado.get(i).getNumero(), "El número de la dirección debe coincidir");
            assertEquals(listaDirecciones.get(i).getPiso(), resultado.get(i).getPiso(), "El piso de la dirección debe coincidir");
            assertEquals(listaDirecciones.get(i).getPuerta(), resultado.get(i).getPuerta(), "La puerta de la dirección debe coincidir");
            assertEquals(listaDirecciones.get(i).getCiudad(), resultado.get(i).getCiudad(), "La ciudad de la dirección debe coincidir");
            assertEquals(listaDirecciones.get(i).getMunicipio(), resultado.get(i).getMunicipio(), "El municipio de la dirección debe coincidir");
            assertEquals(listaDirecciones.get(i).getProvincia(), resultado.get(i).getProvincia(), "La provincia de la dirección debe coincidir");
            assertEquals(listaDirecciones.get(i).getCodigoPostal(), resultado.get(i).getCodigoPostal(), "El código postal de la dirección debe coincidir");
            assertEquals(listaDirecciones.get(i).getActivo(), resultado.get(i).getActivo(), "El estado de la dirección debe coincidir");
        }

        // - Se verifica que ambos repositorios hayan sido llamados 1 vez.
        verify(usuarioRepository, times(1)).existsById(idUsuario);
        verify(direccionRepository, times(1)).findDireccionsByUsuario_IdUsuario(idUsuario);
    }

    @Test
    @DisplayName("Es 1.b: Obtener todas las direcciones de un usuario (sin contenido)")
    void deberiaDevolverUnaListaDeDireccionesVaciaDeUnUsuario_cuandoNoExistanDatos() {
        // Given: (Preparación)
        // - Creamos un identificador de usuario de prueba
        final Long idUsuario = 1L;

        // - Mock configurado para simular que el usuario existe en la base de datos.
        when(usuarioRepository.existsById(idUsuario)).thenReturn(true);

        // - Mock configurado para devolver una lista de direcciones de prueba.
        when(direccionRepository.findDireccionsByUsuario_IdUsuario(idUsuario)).thenReturn(List.of());

        // When: (Ejecución)
        List<DireccionDTOId> resultado = direccionAdminServiceImpl.getDireccionesByUsuarioId(idUsuario);

        // Then: (Verificación)
        // - Verificar que el listado no sea nulo, que la longitud sea correcta y que los datos sean los esperados
        assertNotNull(resultado, "El resultado no debería ser nulo");
        assertEquals(0, resultado.size(),
                "La lista de direcciones debería estar vacía ya que no hay direcciones asociadas al usuario.");

        // - Se verifica que ambos repositorios hayan sido llamados 1 vez.
        verify(usuarioRepository, times(1)).existsById(idUsuario);
        verify(direccionRepository, times(1)).findDireccionsByUsuario_IdUsuario(idUsuario);
    }

    @Test
    @DisplayName("Es 2: Usuario no encontrado al obtener sus direcciones")
    void deberiaLanzarUnaExcepcion_cuandoElUsuarioNoExista() {
        // Given: (Preparación)
        // - Creamos un identificador de usuario de prueba
        final Long idUsuario = 1L;

        // - Mock configurado para lanzar excepción
        when(usuarioRepository.existsById(idUsuario)).thenReturn(false);

        // When & // Then: (Ejecución y verificación)

        // - Verificar que se lanza una excepción UsuarioNotFoundException con el mensaje esperado
        final RuntimeException exception = assertThrows(UsuarioNotFoundException.class,
            () -> direccionAdminServiceImpl.getDireccionesByUsuarioId(idUsuario)
        );

        // - Verificar que el mensaje de la excepción sea el esperado
        assertEquals("No se encontró un usuario con el id: " + idUsuario, exception.getMessage(),
                "El mensaje de la excepción debe coincidir con el esperado");

        // - Verificar que el metodo del repositorio usuario fue llamado exactamente una vez
        verify(usuarioRepository,times(1)).existsById(idUsuario);

        // - Verificar que ningún metodo del repositorio de direcciones no fue llamado
        verifyNoInteractions(direccionRepository);
    }

    @Test
    @DisplayName("Es 3: Identificador nulo")
    void deberiaLanzarUnaExcepcion_cuandoElIdentificadorEsNulo() {
        // Given: (Preparación)
        // - Mock configurado para lanzar excepción
        when(usuarioRepository.existsById(null))
                .thenThrow(new RuntimeException("El identificador del usuario no puede ser nulo"));

        // When & // Then: (Ejecución y verificación)

        // - Verificar que se lanza una excepción RuntimeException con el mensaje esperado
        final RuntimeException exception = assertThrows(RuntimeException.class,
                () -> direccionAdminServiceImpl.getDireccionesByUsuarioId(null)
        );

        // - Verificar que el mensaje de la excepción sea el esperado
        assertEquals("El identificador del usuario no puede ser nulo", exception.getMessage(),
                "El mensaje de la excepción debe coincidir con el esperado");

        // - Verificar que el metodo del repositorio usuario fue llamado exactamente una vez
        verify(usuarioRepository,times(1)).existsById(null);

        // - Verificar que ningún metodo del repositorio de direcciones no fue llamado
        verifyNoInteractions(direccionRepository);
    }

    @Test
    @DisplayName("Es 4.a: Error base de datos al obtener al usuario")
    void deberiaLanzarUnaExcepcion_cuandoOcurreUnErrorEnLaBaseDeDatosAlObtenerAlUsuario() {
        // Given: (Preparación)
        // - Creamos un identificador de usuario de prueba
        final Long idUsuario = 1L;

        // - Mock configurado para lanzar excepción
        when(usuarioRepository.existsById(idUsuario))
                .thenThrow(new RuntimeException("Error de conexión con la base de datos"));

        // When & // Then: (Ejecución y verificación)

        // - Verificar que se lanza una excepción RuntimeException con el mensaje esperado
        final RuntimeException exception = assertThrows(RuntimeException.class,
                () -> direccionAdminServiceImpl.getDireccionesByUsuarioId(idUsuario)
        );

        // - Verificar que el mensaje de la excepción sea el esperado
        assertEquals("Error de conexión con la base de datos", exception.getMessage(),
                "El mensaje de la excepción debe coincidir con el esperado");

        // - Verificar que el metodo del repositorio usuario fue llamado exactamente una vez
        verify(usuarioRepository,times(1)).existsById(idUsuario);

        // - Verificar que ningún metodo del repositorio de direcciones no fue llamado
        verifyNoInteractions(direccionRepository);
    }

    @Test
    @DisplayName("Es 4.b: Error base de datos al buscar las direcciones de un usuario")
    void deberiaLanzarUnaExcepcion_cuandoOcurreUnErrorEnLaBaseDeDatosAlBuscarLasDireccionesDeUnUsuario() {
        // Given: (Preparación)
        // - Creamos un identificador de usuario de prueba
        final Long idUsuario = 1L;

        // - Mock configurado para simular que el usuario existe en la base de datos.
        when(usuarioRepository.existsById(idUsuario)).thenReturn(true);

        // - Mock configurado para lanzar excepción al buscar las direcciones.
        when(direccionRepository.findDireccionsByUsuario_IdUsuario(idUsuario))
                .thenThrow(new RuntimeException("Error de conexión con la base de datos"));

        // When & // Then: (Ejecución y verificación)

        // - Verificar que se lanza una excepción RuntimeException con el mensaje esperado
        final RuntimeException exception = assertThrows(RuntimeException.class,
                () -> direccionAdminServiceImpl.getDireccionesByUsuarioId(idUsuario)
        );

        // - Verificar que el mensaje de la excepción sea el esperado
        assertEquals("Error de conexión con la base de datos", exception.getMessage(),
                "El mensaje de la excepción debe coincidir con el esperado");

        // - Se verifica que ambos repositorios hayan sido llamados 1 vez.
        verify(usuarioRepository, times(1)).existsById(idUsuario);
        verify(direccionRepository, times(1)).findDireccionsByUsuario_IdUsuario(idUsuario);
    }
}
