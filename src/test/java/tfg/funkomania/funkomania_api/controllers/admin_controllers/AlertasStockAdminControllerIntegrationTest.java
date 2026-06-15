package tfg.funkomania.funkomania_api.controllers.admin_controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.persistence.EntityManager;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;
import tfg.funkomania.funkomania_api.persistence.entities.Producto;
import tfg.funkomania.funkomania_api.persistence.repositories.ICategoriaRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IProductoRepository;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AlertasStockAdminControllerIntegrationTest {

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

    @Test
    void getAlertasStock_ConDatos_DeberiaRetornarProductosEnAlerta() throws Exception {
        // 1. Insertar categoría
        Categoria cat = Categoria.builder().nombre("Categoria Test").build();
        cat = categoriaRepository.saveAndFlush(cat);
        Long idCategoria = cat.getId();
        
        // 2. Insertar producto con stock bajo (asumiendo que esto dispara la alerta)
        Producto prod = Producto.builder()
                .nombre("Producto Alerta")
                .precio(BigDecimal.valueOf(10.0))
                .stock(1)
                .iva(BigDecimal.valueOf(21.0))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.valueOf(0.0))
                .categoria(cat)
                .build();
        productoRepository.saveAndFlush(prod);
        entityManager.clear();
        
        mockMvc.perform(get("/admin/alertas-stock/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$[0].nombre").value("Producto Alerta"));
    }


    @Test
    void getAlertasStock_ComoAdmin_DeberiaRetornar200() throws Exception {
        mockMvc.perform(get("/admin/alertas-stock/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void getAlertasStock_SinAutenticacion_DeberiaRetornar401() throws Exception {
        mockMvc.perform(get("/admin/alertas-stock/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAlertasStock_ComoUsuarioNormal_DeberiaRetornar403() throws Exception {
        mockMvc.perform(get("/admin/alertas-stock/")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }
}
