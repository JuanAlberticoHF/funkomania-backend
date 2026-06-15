package tfg.funkomania.funkomania_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.CrearPedidoRequestDTO;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.repositories.*;
import tfg.funkomania.funkomania_api.testutils.UsuarioTestFactory;
import tools.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IPedidoRepository pedidoRepository;

    @Autowired
    private IDetallePedidoRepository detallePedidoRepository;

    @Autowired
    private ICarritoRepository carritoRepository;

    @Autowired
    private IDetalleCarritoRepository detalleCarritoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void limpiarBaseDeDatos() {
        detallePedidoRepository.deleteAll();
        pedidoRepository.deleteAll();
        detalleCarritoRepository.deleteAll();
        carritoRepository.deleteAll();
        usuarioRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    void crearPedido_CarritoVacio_Conflicto() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuarioRepository.save(usuario);

        // Crear carrito para el usuario
        tfg.funkomania.funkomania_api.persistence.entities.Carrito carrito = new tfg.funkomania.funkomania_api.persistence.entities.Carrito();
        carrito.setUsuario(usuario);
        carrito.setFechaCreacion(java.time.LocalDateTime.now());
        carrito.setFechaActualizacion(java.time.LocalDateTime.now());
        carrito.setEstado(tfg.funkomania.funkomania_api.persistence.enums.EstadoCarritoEnum.ACTIVO);
        carritoRepository.save(carrito);
        usuario.setCarrito(carrito);
        usuarioRepository.save(usuario);

        CrearPedidoRequestDTO request = new CrearPedidoRequestDTO(1L, 1L, "Comentario");

        mockMvc.perform(post("/pedidos/crear-desde-pedido")
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void obtenerPedidosUsuario_Exitoso() throws Exception {
        Usuario usuario = UsuarioTestFactory.usuarioPersistible("test@example.com", "password");
        usuarioRepository.save(usuario);

        mockMvc.perform(get("/usuario/pedidos")
                        .with(SecurityMockMvcRequestPostProcessors.user(usuario.getEmail()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
