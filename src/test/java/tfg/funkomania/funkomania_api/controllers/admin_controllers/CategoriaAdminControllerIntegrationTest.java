package tfg.funkomania.funkomania_api.controllers.admin_controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.persistence.EntityManager;
import tfg.funkomania.funkomania_api.dtos.categoria_dtos.CategoriaDTORequest;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;
import tfg.funkomania.funkomania_api.persistence.entities.Producto;
import tfg.funkomania.funkomania_api.persistence.repositories.ICategoriaRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IProductoRepository;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoriaAdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void limpiarBaseDeDatos() {
        productoRepository.deleteAll();
        categoriaRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    void getAllCategorias_ComoAdmin_DeberiaRetornar200() throws Exception {
        mockMvc.perform(get("/admin/categorias/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void getProductosAsociados_ComoAdmin_DeberiaRetornar200() throws Exception {
        Categoria cat = Categoria.builder().nombre("Cat1").build();
        cat = categoriaRepository.saveAndFlush(cat);
        Long idCat = cat.getId();
        
        Producto prod = Producto.builder()
                .nombre("Prod1")
                .precio(BigDecimal.valueOf(10.0))
                .stock(10)
                .iva(BigDecimal.valueOf(21.0))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.valueOf(0.0))
                .categoria(cat)
                .build();
        productoRepository.saveAndFlush(prod);
        entityManager.clear();
        
        mockMvc.perform(get("/admin/categorias/" + idCat)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Prod1"));
    }


    @Test
    void getProductosAsociados_CategoriaInexistente_DeberiaRetornar404() throws Exception {
        mockMvc.perform(get("/admin/categorias/999")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearCategoria_ComoAdmin_DeberiaRetornarOk() throws Exception {
        CategoriaDTORequest request = new CategoriaDTORequest("Nueva Cat", 0L);
        
        mockMvc.perform(post("/admin/categorias/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        entityManager.clear();
        long count = categoriaRepository.findAll().stream().filter(c -> c.getNombre().equals("Nueva Cat")).count();
        org.junit.jupiter.api.Assertions.assertEquals(1, count);
    }

    @Test
    void crearCategoria_PadreInexistente_DeberiaRetornar404() throws Exception {
        CategoriaDTORequest request = new CategoriaDTORequest("Nueva Cat", 999L);
        
        mockMvc.perform(post("/admin/categorias/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarCategoria_ComoAdmin_DeberiaRetornarOk() throws Exception {
        Categoria cat = Categoria.builder().nombre("Vieja Cat").build();
        cat = categoriaRepository.saveAndFlush(cat);
        Long idCat = cat.getId();
        
        CategoriaDTORequest request = new CategoriaDTORequest("Nueva Cat", 0L);
        
        mockMvc.perform(put("/admin/categorias/" + idCat)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        entityManager.clear();
        Categoria updatedCat = categoriaRepository.findById(idCat).orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals("Nueva Cat", updatedCat.getNombre());
    }

    @Test
    void eliminarCategoria_ComoAdmin_DeberiaRetornarOk() throws Exception {
        Categoria cat = Categoria.builder().nombre("Eliminar Cat").build();
        cat = categoriaRepository.saveAndFlush(cat);
        Long idCat = cat.getId();
        
        mockMvc.perform(delete("/admin/categorias/" + idCat)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        
        entityManager.clear();
        boolean exists = categoriaRepository.existsById(idCat);
        org.junit.jupiter.api.Assertions.assertEquals(false, exists);
    }

    @Test
    void eliminarCategoria_ConProductos_DeberiaRetornar409() throws Exception {
        Categoria cat = Categoria.builder().nombre("Cat Con Prod").build();
        cat = categoriaRepository.saveAndFlush(cat);
        Long idCat = cat.getId();
        
        Producto prod = Producto.builder()
                .nombre("Prod")
                .precio(BigDecimal.valueOf(10.0))
                .stock(10)
                .iva(BigDecimal.valueOf(21.0))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.valueOf(0.0))
                .categoria(cat)
                .build();
        productoRepository.saveAndFlush(prod);
        
        mockMvc.perform(delete("/admin/categorias/" + idCat)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isConflict());
    }


    @Test
    void accederSinToken_DeberiaRetornar401() throws Exception {
        mockMvc.perform(get("/admin/categorias/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accederComoUser_DeberiaRetornar403() throws Exception {
        mockMvc.perform(get("/admin/categorias/")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }
}
