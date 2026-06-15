package tfg.funkomania.funkomania_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.persistence.entities.*;
import tfg.funkomania.funkomania_api.persistence.repositories.*;
import tfg.funkomania.funkomania_api.testutils.UsuarioTestFactory;
import tools.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ListaDeseosControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void limpiarBaseDeDatos() {
        usuarioRepository.deleteAllInBatch();
        productoRepository.deleteAllInBatch();
        categoriaRepository.deleteAllInBatch();
        entityManager.clear();
    }

    @Test
    void obtenerListaDeseosDelUsuario_Exitoso() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuario.setProductosDeseados(new HashSet<>());
        usuarioRepository.saveAndFlush(usuario);

        mockMvc.perform(get("/usuario/lista-deseos/")
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addProductoAListaDeseos_Exitoso() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuario.setProductosDeseados(new HashSet<>());
        usuario = usuarioRepository.saveAndFlush(usuario);
        
        Categoria cat = Categoria.builder().nombre("Cat").build();
        categoriaRepository.saveAndFlush(cat);
        
        Producto prod = Producto.builder()
                .nombre("Producto")
                .precio(BigDecimal.TEN)
                .stock(10)
                .iva(BigDecimal.valueOf(21))
                .categoria(cat)
                .activo(true)
                .descuento(BigDecimal.ZERO)
                .build();
        prod = productoRepository.saveAndFlush(prod);

        mockMvc.perform(post("/usuario/lista-deseos/{idProducto}", prod.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarProductoDeListaDeseos_Exitoso() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuario.setProductosDeseados(new HashSet<>());
        
        Categoria cat = Categoria.builder().nombre("Cat").build();
        categoriaRepository.saveAndFlush(cat);
        
        Producto prod = Producto.builder()
                .nombre("Producto")
                .precio(BigDecimal.TEN)
                .stock(10)
                .iva(BigDecimal.valueOf(21))
                .categoria(cat)
                .activo(true)
                .descuento(BigDecimal.ZERO)
                .build();
        prod = productoRepository.saveAndFlush(prod);
        
        usuario.getProductosDeseados().add(prod);
        usuario = usuarioRepository.saveAndFlush(usuario);

        mockMvc.perform(delete("/usuario/lista-deseos/{idProducto}", prod.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
