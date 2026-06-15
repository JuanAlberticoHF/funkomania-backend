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
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;
import tfg.funkomania.funkomania_api.persistence.entities.Producto;
import tfg.funkomania.funkomania_api.persistence.repositories.ICategoriaRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IProductoRepository;
import java.math.BigDecimal;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.ProductoDTOIdCategoria;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoAdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

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
    void getAllProductos_ComoAdmin_DeberiaRetornar200() throws Exception {
        mockMvc.perform(get("/admin/productos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void getAllProductos_ConBusqueda_DeberiaFiltrar() throws Exception {
        Categoria cat = Categoria.builder().nombre("Cat").build();
        cat = categoriaRepository.saveAndFlush(cat);
        Long idCat = cat.getId();
        
        Producto p1 = Producto.builder()
                .nombre("Spider-Man Figure")
                .precio(BigDecimal.valueOf(10.0))
                .stock(10)
                .iva(BigDecimal.valueOf(21.0))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.valueOf(0.0))
                .categoria(cat)
                .build();
        Producto p2 = Producto.builder()
                .nombre("Batman Figure")
                .precio(BigDecimal.valueOf(10.0))
                .stock(10)
                .iva(BigDecimal.valueOf(21.0))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.valueOf(0.0))
                .categoria(cat)
                .build();
        productoRepository.saveAll(java.util.List.of(p1, p2));
        entityManager.clear();

        mockMvc.perform(get("/admin/productos/")
                        .param("search", "Spider")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Spider-Man Figure"));
    }

    @Test
    void addProducto_ComoAdmin_DeberiaRetornar201() throws Exception {
        Categoria cat = Categoria.builder().nombre("Cat").build();
        cat = categoriaRepository.saveAndFlush(cat);
        Long idCat = cat.getId();
        
        ProductoDTOIdCategoria request = new ProductoDTOIdCategoria(idCat, "Nuevo Prod", BigDecimal.valueOf(15.0), 100, "img.jpg", "Desc", BigDecimal.valueOf(21.0), true, false, BigDecimal.valueOf(0.0), null);
        
        mockMvc.perform(post("/admin/productos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        
        entityManager.clear();
        long count = productoRepository.findAll().stream().filter(p -> p.getNombre().equals("Nuevo Prod")).count();
        org.junit.jupiter.api.Assertions.assertEquals(1, count);
    }

    @Test
    void addProducto_CategoriaInexistente_DeberiaRetornar404() throws Exception {
        ProductoDTOIdCategoria request = new ProductoDTOIdCategoria(999L, "Nuevo Prod", BigDecimal.valueOf(15.0), 100, "img.jpg", "Desc", BigDecimal.valueOf(21.0), true, false, BigDecimal.valueOf(0.0), null);
        
        mockMvc.perform(post("/admin/productos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProducto_ComoAdmin_DeberiaRetornarOk() throws Exception {
        Categoria cat = Categoria.builder().nombre("Cat").build();
        cat = categoriaRepository.saveAndFlush(cat);
        Long idCat = cat.getId();
        
        Producto prod = Producto.builder()
                .nombre("Prod Original")
                .precio(BigDecimal.valueOf(10.0))
                .stock(10)
                .iva(BigDecimal.valueOf(21.0))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.valueOf(0.0))
                .categoria(cat)
                .build();
        prod = productoRepository.saveAndFlush(prod);
        Long idProd = prod.getId();
        
        ProductoDTOIdCategoria request = new ProductoDTOIdCategoria(idCat, "Prod Actualizado", BigDecimal.valueOf(20.0), 50, "img2.jpg", "Desc 2", BigDecimal.valueOf(21.0), true, false, BigDecimal.valueOf(0.0), null);
        
        mockMvc.perform(put("/admin/productos/" + idProd)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        entityManager.clear();
        Producto updatedProd = productoRepository.findById(idProd).orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals("Prod Actualizado", updatedProd.getNombre());
    }

    @Test
    void updateProducto_Inexistente_DeberiaRetornar404() throws Exception {
        Categoria cat = Categoria.builder().nombre("Cat").build();
        cat = categoriaRepository.saveAndFlush(cat);
        Long idCat = cat.getId();
        ProductoDTOIdCategoria request = new ProductoDTOIdCategoria(idCat, "Nombre", BigDecimal.valueOf(10.0), 10, "img.jpg", "Desc", BigDecimal.valueOf(21.0), true, false, BigDecimal.valueOf(0.0), null);
        
        mockMvc.perform(put("/admin/productos/999")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProducto_ComoAdmin_DeberiaRetornarOk() throws Exception {
        Categoria cat = Categoria.builder().nombre("Cat").build();
        cat = categoriaRepository.saveAndFlush(cat);
        Long idCat = cat.getId();
        
        Producto prod = Producto.builder()
                .nombre("Prod Eliminar")
                .precio(BigDecimal.valueOf(10.0))
                .stock(10)
                .iva(BigDecimal.valueOf(21.0))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.valueOf(0.0))
                .categoria(cat)
                .build();
        prod = productoRepository.saveAndFlush(prod);
        Long idProd = prod.getId();
        
        mockMvc.perform(delete("/admin/productos/" + idProd)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        
        entityManager.clear();
        Producto deletedProd = productoRepository.findById(idProd).orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals(false, deletedProd.isActivo());
    }

    @Test
    void deleteProducto_Inexistente_DeberiaRetornar404() throws Exception {
        mockMvc.perform(delete("/admin/productos/999")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    void accederSinToken_DeberiaRetornar401() throws Exception {
        mockMvc.perform(get("/admin/productos/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accederComoUser_DeberiaRetornar403() throws Exception {
        mockMvc.perform(get("/admin/productos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }
}
