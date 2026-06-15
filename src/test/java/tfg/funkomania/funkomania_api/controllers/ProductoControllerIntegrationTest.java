package tfg.funkomania.funkomania_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;
import tfg.funkomania.funkomania_api.persistence.entities.Producto;
import tfg.funkomania.funkomania_api.persistence.repositories.ICategoriaRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IProductoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración para el controlador de productos.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void limpiarBaseDeDatos() {
        productoRepository.deleteAll();
        categoriaRepository.deleteAll();
        entityManager.clear();
    }

    private Long crearCategoria(String nombre) {
        Categoria cat = Categoria.builder().nombre(nombre).build();
        cat = categoriaRepository.saveAndFlush(cat);
        return cat.getId();
    }

    private void crearProducto(String nombre, BigDecimal precio, Integer stock, boolean activo, boolean oferta, BigDecimal descuento, LocalDateTime fechaFin, Long catId) {
        Producto p = Producto.builder()
                .nombre(nombre)
                .precio(precio)
                .stock(stock)
                .iva(BigDecimal.valueOf(21))
                .activo(activo)
                .enOferta(oferta)
                .descuento(descuento)
                .fechaFinOferta(fechaFin)
                .categoria(categoriaRepository.findById(catId).orElseThrow())
                .build();
        productoRepository.saveAndFlush(p);
    }

    @Test
    void getAllProductos_SinFiltros_DeberiaDevolverTodosLosActivos() {
        Long catId = crearCategoria("Cat1");
        crearProducto("Prod1", BigDecimal.valueOf(10), 5, true, false, BigDecimal.ZERO, null, catId);
        crearProducto("Prod2", BigDecimal.valueOf(20), 3, true, false, BigDecimal.ZERO, null, catId);
        crearProducto("ProdInactivo", BigDecimal.valueOf(30), 2, false, false, BigDecimal.ZERO, null, catId);
        
        entityManager.clear();

        try {
            mockMvc.perform(get("/productos/")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllProductos_ConFiltros_DeberiaFiltrarCorrectamente() {
        Long cat1 = crearCategoria("Cat1");
        Long cat2 = crearCategoria("Cat2");
        
        crearProducto("Alpha", BigDecimal.valueOf(10), 5, true, false, BigDecimal.ZERO, null, cat1);
        crearProducto("Beta", BigDecimal.valueOf(50), 5, true, false, BigDecimal.ZERO, null, cat1);
        crearProducto("Gamma", BigDecimal.valueOf(100), 5, true, false, BigDecimal.ZERO, null, cat2);
        
        entityManager.clear();

        try {
            mockMvc.perform(get("/productos/")
                            .param("search", "Alpha")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.content[0].nombre").value("Alpha"));

            mockMvc.perform(get("/productos/")
                            .param("idCategoria", cat2.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.content[0].nombre").value("Gamma"));

            mockMvc.perform(get("/productos/")
                            .param("precioMin", "40")
                            .param("precioMax", "60")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.content[0].nombre").value("Beta"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllProductos_PaginacionYOrden_DeberiaFuncionar() {
        Long catId = crearCategoria("Cat");
        for (int i = 1; i <= 15; i++) {
            crearProducto("Prod" + String.format("%02d", i), BigDecimal.valueOf(10), 5, true, false, BigDecimal.ZERO, null, catId);
        }
        entityManager.clear();

        try {
            mockMvc.perform(get("/productos/")
                            .param("page", "0")
                            .param("size", "5")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numberOfElements").value(5))
                    .andExpect(jsonPath("$.totalElements").value(15));

            mockMvc.perform(get("/productos/")
                            .param("sort", "nombre,desc")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].nombre").value("Prod15"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllProductosOfertas_DeberiaRetornarSoloOfertasVigentesYActivas() {
        Long catId = crearCategoria("Cat");
        
        crearProducto("OfertaOk", BigDecimal.valueOf(10), 5, true, true, BigDecimal.valueOf(10), LocalDateTime.now().plusDays(1), catId);
        crearProducto("OfertaCaducada", BigDecimal.valueOf(10), 5, true, true, BigDecimal.valueOf(10), LocalDateTime.now().minusDays(1), catId);
        crearProducto("NoOferta", BigDecimal.valueOf(10), 5, true, false, BigDecimal.ZERO, null, catId);
        crearProducto("OfertaInactivo", BigDecimal.valueOf(10), 5, false, true, BigDecimal.valueOf(10), LocalDateTime.now().plusDays(1), catId);
        
        entityManager.clear();

        try {
            mockMvc.perform(get("/productos/ofertas")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.content[0].nombre").value("OfertaOk"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllProductos_ParametrosInvalidos_DeberiaRetornar400() {
        try {
            mockMvc.perform(get("/productos/")
                            .param("idCategoria", "abc")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            mockMvc.perform(get("/productos/")
                            .param("precioMin", "abc")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            mockMvc.perform(get("/productos/")
                            .param("precioMax", "abc")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getProductoById_Exitoso_DeberiaRetornarProducto() {
        Long catId = crearCategoria("Cat");
        Producto p = Producto.builder()
                .nombre("UniqueProd")
                .precio(BigDecimal.valueOf(10))
                .stock(5)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.ZERO)
                .categoria(categoriaRepository.findById(catId).orElseThrow())
                .build();
        p = productoRepository.saveAndFlush(p);
        entityManager.clear();

        try {
            mockMvc.perform(get("/productos/{id}", p.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(p.getId().intValue()))
                    .andExpect(jsonPath("$.nombre").value("UniqueProd"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getProductoById_NoEncontrado_DeberiaRetornar404() {
        try {
            mockMvc.perform(get("/productos/{id}", 999999L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getProductoById_IdInvalido_DeberiaRetornar400() {
        try {
            mockMvc.perform(get("/productos/{id}", "abc")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
