package tfg.funkomania.funkomania_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tfg.funkomania.funkomania_api.persistence.repositories.ICategoriaRepository;

/**
 * Pruebas de integración para el controlador de categorías.
 *
 * <p>Ejecuta peticiones HTTP simuladas con MockMvc y valida respuestas JSON y códigos HTTP
 * contra el contexto real de Spring Boot.</p>
 *
 * @version 1.0.0
 * @since 0.2.0
 */
@SpringBootTest
@AutoConfigureMockMvc
class CategoriaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        categoriaRepository.deleteAll();
    }

    // Debe retornar una lista de todas las categorías disponibles en el sistema, incluyendo su categoría padre si la tienen.
    @Test
    void obtenerTodasLasCategorias_DeberiaDevolverTodasLasCategorias() {
        // Crear categoria padre y categoria hija
        tfg.funkomania.funkomania_api.persistence.entities.Categoria padre = tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                .nombre("Padre")
                .build();
        padre = categoriaRepository.save(padre);

        tfg.funkomania.funkomania_api.persistence.entities.Categoria hija = tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                .nombre("Hija")
                .categoriaPadre(padre)
                .build();
        categoriaRepository.save(hija);

        try {
            mockMvc.perform(get("/categorias/").contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nombre").isNotEmpty())
                    .andExpect(jsonPath("$[1].nombre").isNotEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
