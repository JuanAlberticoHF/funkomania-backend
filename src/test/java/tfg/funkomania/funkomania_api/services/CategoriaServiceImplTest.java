package tfg.funkomania.funkomania_api.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link CategoriaServiceImpl}.
 *
 * @version 1.0.0
 * @since 0.2.0
 */
class CategoriaServiceImplTest {
    /**
     * Debe devolver todas las categorías disponibles en el sistema.
     */
    @Test
    void obtenerTodasLasCategorias_DeberiaDevolverTodasLasCategorias() {
        tfg.funkomania.funkomania_api.persistence.repositories.ICategoriaRepository repo = Mockito.mock(tfg.funkomania.funkomania_api.persistence.repositories.ICategoriaRepository.class);
        tfg.funkomania.funkomania_api.persistence.entities.Categoria c = tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder().id(1L).nombre("X").build();
        Mockito.when(repo.findAll()).thenReturn(List.of(c));

        CategoriaServiceImpl s = new CategoriaServiceImpl(repo);
        List<tfg.funkomania.funkomania_api.persistence.entities.Categoria> result = s.getAllCategorias();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("X", result.getFirst().getNombre());
    }
}
