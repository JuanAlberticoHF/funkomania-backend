package tfg.funkomania.funkomania_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.persistence.EntityManager;
import tfg.funkomania.funkomania_api.persistence.repositories.ICategoriaRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IProductoRepository;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración para el controlador de categorías.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CategoriaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void limpiarBaseDeDatos() {
        productoRepository.deleteAll();
        categoriaRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    void obtenerTodasLasCategorias_DeberiaDevolverListaCuandoExistenDatos() {
        Categoria padre = Categoria.builder()
                .nombre("Electrónica")
                .build();
        padre = categoriaRepository.save(padre);

        Categoria hija = Categoria.builder()
                .nombre("Móviles")
                .categoriaPadre(padre)
                .build();
        categoriaRepository.save(hija);

        try {
            mockMvc.perform(get("/categorias/")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[?(@.nombre == 'Móviles')].categoriaPadre.nombre").value("Electrónica"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void obtenerTodasLasCategorias_DeberiaDevolverListaVaciaCuandoNoHayDatos() {
        try {
            mockMvc.perform(get("/categorias/")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void obtenerTodasLasCategorias_DeberiaDevolverCategoriaSimple() {
        Categoria cat = Categoria.builder()
                .nombre("Categoria Simple")
                .build();
        categoriaRepository.save(cat);
        entityManager.clear();

        try {
            mockMvc.perform(get("/categorias/")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].nombre").value("Categoria Simple"))
                    .andExpect(jsonPath("$[0].categoriaPadre").isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
