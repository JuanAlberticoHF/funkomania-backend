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
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.*;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPagoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPedidoEnum;
import tfg.funkomania.funkomania_api.persistence.entities.*;
import tfg.funkomania.funkomania_api.persistence.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoAdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IDireccionRepository direccionRepository;

    @Autowired
    private IMetodoPagoRepository metodoPagoRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private IPedidoRepository pedidoRepository;

    @Autowired
    private IDetallePedidoRepository detallePedidoRepository;

    @Autowired
    private EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void limpiarBaseDeDatos() {
        detallePedidoRepository.deleteAll();
        pedidoRepository.deleteAll();
        productoRepository.deleteAll();
        categoriaRepository.deleteAll();
        direccionRepository.deleteAll();
        metodoPagoRepository.deleteAll();
        usuarioRepository.deleteAll();
        entityManager.clear();
    }

    private void setupBasicData() {
        Usuario user = Usuario.builder()
                .nombre("User")
                .apellido1("Test")
                .email("user@test.com")
                .password("pass")
                .activo(true)
                .rol(tfg.funkomania.funkomania_api.persistence.enums.RoleEnum.CLIENTE)
                .fechaRegistro(LocalDateTime.now())
                .build();
        usuarioRepository.saveAndFlush(user);
        
        Direccion dir = Direccion.builder()
                .calle("Calle 1")
                .numero("10")
                .ciudad("City")
                .municipio("Mun")
                .provincia("Prov")
                .codigoPostal("12345")
                .activo(true)
                .usuario(user)
                .build();

        direccionRepository.saveAndFlush(dir);
        
        MetodoPago met = MetodoPago.builder()
                .nombre("Card")
                .activo(true)
                .build();
        metodoPagoRepository.saveAndFlush(met);
        
        Categoria cat = Categoria.builder().nombre("Cat").build();
        categoriaRepository.saveAndFlush(cat);
        
        Producto prod = Producto.builder()
                .nombre("Prod1")
                .precio(BigDecimal.valueOf(10.0))
                .stock(100)
                .iva(BigDecimal.valueOf(21.0))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.valueOf(0.0))
                .categoria(cat)
                .build();
        productoRepository.saveAndFlush(prod);
        entityManager.clear();
    }

    @Test
    void getAllPedidosAdmin_ComoAdmin_DeberiaRetornar200() throws Exception {
        setupBasicData();
        mockMvc.perform(get("/admin/pedidos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void crearPedidoParaUsuario_ComoAdmin_DeberiaRetornar201() throws Exception {
        setupBasicData();
        Usuario user = usuarioRepository.findAll().stream().findFirst().orElseThrow();
        Direccion dir = direccionRepository.findAll().stream().findFirst().orElseThrow();
        MetodoPago met = metodoPagoRepository.findAll().stream().findFirst().orElseThrow();
        Producto prodEnt = productoRepository.findAll().stream().findFirst().orElseThrow();
        
        AdminProductoDTO prod = new AdminProductoDTO(prodEnt.getId(), 1, BigDecimal.valueOf(10.0), BigDecimal.valueOf(21.0));
        CrearPedidoAdminRequestDTO request = new CrearPedidoAdminRequestDTO(user.getIdUsuario(), dir.getId(), met.getId(), EstadoPedidoEnum.PENDIENTE, EstadoPagoEnum.PENDIENTE, "Comentario", List.of(prod));
        
        mockMvc.perform(post("/admin/pedidos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        
        entityManager.clear();
        long count = pedidoRepository.count();
        org.junit.jupiter.api.Assertions.assertEquals(1, count);
    }


    @Test
    void crearPedidoParaUsuario_StockInsuficiente_DeberiaRetornar400() throws Exception {
        setupBasicData();
        Usuario user = usuarioRepository.findAll().stream().findFirst().orElseThrow();
        Direccion dir = direccionRepository.findAll().stream().findFirst().orElseThrow();
        MetodoPago met = metodoPagoRepository.findAll().stream().findFirst().orElseThrow();
        Producto prodEnt = productoRepository.findAll().stream().findFirst().orElseThrow();
        
        AdminProductoDTO prod = new AdminProductoDTO(prodEnt.getId(), 1000, BigDecimal.valueOf(10.0), BigDecimal.valueOf(21.0));
        CrearPedidoAdminRequestDTO request = new CrearPedidoAdminRequestDTO(user.getIdUsuario(), dir.getId(), met.getId(), EstadoPedidoEnum.PENDIENTE, EstadoPagoEnum.PENDIENTE, "Comentario", List.of(prod));
        
        mockMvc.perform(post("/admin/pedidos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void obtenerPedidoEnAdminPorId_ComoAdmin_DeberiaRetornar200() throws Exception {
        setupBasicData();
        Usuario user = usuarioRepository.findAll().stream().findFirst().orElseThrow();
        Direccion dir = direccionRepository.findAll().stream().findFirst().orElseThrow();
        MetodoPago met = metodoPagoRepository.findAll().stream().findFirst().orElseThrow();
        Producto prodEnt = productoRepository.findAll().stream().findFirst().orElseThrow();
        
        AdminProductoDTO prod = new AdminProductoDTO(prodEnt.getId(), 1, BigDecimal.valueOf(10.0), BigDecimal.valueOf(21.0));
        CrearPedidoAdminRequestDTO request = new CrearPedidoAdminRequestDTO(user.getIdUsuario(), dir.getId(), met.getId(), EstadoPedidoEnum.PENDIENTE, EstadoPagoEnum.PENDIENTE, "Comentario", List.of(prod));
        
        mockMvc.perform(post("/admin/pedidos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        
        entityManager.clear();
        Long idPedido = pedidoRepository.findAll().stream().findFirst().orElseThrow().getIdPedido();
        
        mockMvc.perform(get("/admin/pedidos/" + idPedido)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPedido").value(idPedido));
    }


    @Test
    void actualizarPedido_ComoAdmin_DeberiaRetornar200() throws Exception {
        setupBasicData();
        Usuario user = usuarioRepository.findAll().stream().findFirst().orElseThrow();
        Direccion dir = direccionRepository.findAll().stream().findFirst().orElseThrow();
        MetodoPago met = metodoPagoRepository.findAll().stream().findFirst().orElseThrow();
        Producto prodEnt = productoRepository.findAll().stream().findFirst().orElseThrow();
        
        AdminProductoDTO prod = new AdminProductoDTO(prodEnt.getId(), 1, BigDecimal.valueOf(10.0), BigDecimal.valueOf(21.0));
        CrearPedidoAdminRequestDTO request = new CrearPedidoAdminRequestDTO(user.getIdUsuario(), dir.getId(), met.getId(), EstadoPedidoEnum.PENDIENTE, EstadoPagoEnum.PENDIENTE, "Comentario", List.of(prod));
        
        mockMvc.perform(post("/admin/pedidos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        
        entityManager.clear();
        Long idPedido = pedidoRepository.findAll().stream().findFirst().orElseThrow().getIdPedido();
        
        AdminUpdatePedidoRequestDTO updateRequest = new AdminUpdatePedidoRequestDTO(EstadoPedidoEnum.PROCESANDO, EstadoPagoEnum.PAGADO, met.getId(), "Nuevo Comentario");
        
        mockMvc.perform(put("/admin/pedidos/" + idPedido)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
        
        entityManager.clear();
        Pedido updatedPedido = pedidoRepository.findById(idPedido).orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals(EstadoPedidoEnum.PROCESANDO, updatedPedido.getEstadoPedido());
    }



    @Test
    void agregarProductoAlPedido_ComoAdmin_DeberiaRetornar200() throws Exception {
        setupBasicData();
        Usuario user = usuarioRepository.findAll().stream().findFirst().orElseThrow();
        Direccion dir = direccionRepository.findAll().stream().findFirst().orElseThrow();
        MetodoPago met = metodoPagoRepository.findAll().stream().findFirst().orElseThrow();
        Producto prodEnt = productoRepository.findAll().stream().findFirst().orElseThrow();
        
        AdminProductoDTO prod = new AdminProductoDTO(prodEnt.getId(), 1, BigDecimal.valueOf(10.0), BigDecimal.valueOf(21.0));
        CrearPedidoAdminRequestDTO request = new CrearPedidoAdminRequestDTO(user.getIdUsuario(), dir.getId(), met.getId(), EstadoPedidoEnum.PENDIENTE, EstadoPagoEnum.PENDIENTE, "Comentario", List.of(prod));
        
        mockMvc.perform(post("/admin/pedidos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        
        entityManager.clear();
        Long idPedido = pedidoRepository.findAll().stream().findFirst().orElseThrow().getIdPedido();
        
        AdminAgregarLineaPedidoRequestDTO lineRequest = new AdminAgregarLineaPedidoRequestDTO(prodEnt.getId(), 2, BigDecimal.valueOf(10.0), BigDecimal.valueOf(21.0));
        
        mockMvc.perform(post("/admin/pedidos/" + idPedido + "/lineas")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lineRequest)))
                .andExpect(status().isOk());
    }



    @Test
    void eliminarDetallePedido_ComoAdmin_DeberiaRetornarOk() throws Exception {
        setupBasicData();
        Usuario user = usuarioRepository.findAll().stream().findFirst().orElseThrow();
        Direccion dir = direccionRepository.findAll().stream().findFirst().orElseThrow();
        MetodoPago met = metodoPagoRepository.findAll().stream().findFirst().orElseThrow();
        Producto prodEnt = productoRepository.findAll().stream().findFirst().orElseThrow();
        
        AdminProductoDTO prod = new AdminProductoDTO(prodEnt.getId(), 1, BigDecimal.valueOf(10.0), BigDecimal.valueOf(21.0));
        CrearPedidoAdminRequestDTO request = new CrearPedidoAdminRequestDTO(user.getIdUsuario(), dir.getId(), met.getId(), EstadoPedidoEnum.PENDIENTE, EstadoPagoEnum.PENDIENTE, "Comentario", List.of(prod));
        
        mockMvc.perform(post("/admin/pedidos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));
        
        entityManager.clear();
        Long idPedido = pedidoRepository.findAll().stream().findFirst().orElseThrow().getIdPedido();
        
        mockMvc.perform(delete("/admin/pedidos/" + idPedido + "/lineas/" + prodEnt.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
        
        entityManager.clear();
        long count = detallePedidoRepository.findAll().stream().filter(dp -> dp.getPedido().getIdPedido().equals(idPedido) && dp.getProducto().getId().equals(prodEnt.getId())).count();
        org.junit.jupiter.api.Assertions.assertEquals(0, count);
    }




    @Test
    void accederSinToken_DeberiaRetornar401() throws Exception {
        mockMvc.perform(get("/admin/pedidos/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void accederComoUser_DeberiaRetornar403() throws Exception {
        mockMvc.perform(get("/admin/pedidos/")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }
}
