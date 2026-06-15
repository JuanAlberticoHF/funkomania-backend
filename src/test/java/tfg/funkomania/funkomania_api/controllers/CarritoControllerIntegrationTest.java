package tfg.funkomania.funkomania_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import tfg.funkomania.funkomania_api.persistence.entities.*;
import tfg.funkomania.funkomania_api.persistence.repositories.*;
import tfg.funkomania.funkomania_api.testutils.UsuarioTestFactory;
import tools.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarritoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ICarritoRepository carritoRepository;

    @Autowired
    private IDetalleCarritoRepository detalleCarritoRepository;

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
        detalleCarritoRepository.deleteAll();
        carritoRepository.deleteAll();
        productoRepository.deleteAll();
        categoriaRepository.deleteAll();
        usuarioRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    void obtenerCarritoDelUsuario_Exitoso() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuarioRepository.save(usuario);

        mockMvc.perform(get("/carrito/")
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void agregarProductoAlCarrito_Exitoso() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuarioRepository.save(usuario);
        
        Categoria cat = Categoria.builder().nombre("Cat").build();
        categoriaRepository.save(cat);
        
        Producto prod = Producto.builder()
                .nombre("Producto")
                .precio(BigDecimal.TEN)
                .stock(10)
                .iva(BigDecimal.valueOf(21))
                .categoria(cat)
                .activo(true)
                .descuento(BigDecimal.ZERO)
                .build();
        productoRepository.save(prod);

        mockMvc.perform(post("/carrito/{idProducto}", prod.getId())
                        .param("cantidad", "1")
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
