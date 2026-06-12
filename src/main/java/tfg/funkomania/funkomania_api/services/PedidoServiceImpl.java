package tfg.funkomania.funkomania_api.services;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.*;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.*;
import tfg.funkomania.funkomania_api.persistence.entities.*;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPagoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPedidoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.TipoNotificacionEnum;
import tfg.funkomania.funkomania_api.persistence.repositories.*;
import tfg.funkomania.funkomania_api.persistence.specifications.PedidosAdminSpecification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>Servicio para gestionar los pedidos de los usuarios.</p>
 * <p>Esta clase implementa la interfaz {@link PedidoService} y {@link PedidoAdminService}.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@Service
public class PedidoServiceImpl implements PedidoService, PedidoAdminService {

    /** Repositorio para acceder a la vista de historial de pedidos del usuario. */
    private final IVistaHistorialPedidosUsuarioRepository vistaHistorialPedidosUsuarioRepository;

    /** Repositorio para acceder a la información de los usuarios. */
    private final IUsuarioRepository usuarioRepository;

    /** Repositorio para acceder a la información de los pedidos en la base de datos. */
    private final IPedidoRepository pedidoRepository;

    /** EntityManager para forzar operaciones y vaciar el contexto */
    private final EntityManager entityManager;

    /** Servicio de notificaciones para enviar notificaciones a los usuarios. */
    private final NotificacionServiceImpl notificacionServiceImpl;

    /** Repositorio para acceder a la información de un pedido */
    private final IVistaPedidoTotalesRepository vistaPedidoTotalesRepository;

    /** Repositorio para acceder a la información de las líneas de pedido */
    private final IVistaDetallePedidoRepository vistaDetallePedidoRepository;
    
    /** Repositorio para acceder a las direcciones */
    private final IDireccionRepository direccionRepository;
    
    /** Repositorio para acceder a los metodos de pago */
    private final IMetodoPagoRepository metodoPagoRepository;

    /** Repositorio para acceder a los productos */
    private final IProductoRepository productoRepository;

    /** Repositorio para acceder a los pedidos */
    private final IDetallePedidoRepository detallePedidoRepository;

    /** Repositorio para acceder a los pedidos */
    private final IVistaPedidosAdminRepository vistaPedidosAdminRepository;

    public PedidoServiceImpl(IVistaHistorialPedidosUsuarioRepository vistaHistorialPedidosUsuarioRepository,
                             IUsuarioRepository usuarioRepository,
                             IPedidoRepository pedidoRepository,
                             EntityManager entityManager,
                             NotificacionServiceImpl notificacionServiceImpl,
                             IVistaDetallePedidoRepository vistaDetallePedidoRepository,
                             IVistaPedidoTotalesRepository vistaPedidoTotalesRepository,
                             IDetallePedidoRepository detallePedidoRepository,
                             IVistaPedidosAdminRepository vistaPedidosAdminRepository,
                             IDireccionRepository direccionRepository,
                             IMetodoPagoRepository metodoPagoRepository,
                             IProductoRepository productoRepository) {
        this.vistaHistorialPedidosUsuarioRepository = vistaHistorialPedidosUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.entityManager = entityManager;
        this.notificacionServiceImpl = notificacionServiceImpl;
        this.vistaDetallePedidoRepository = vistaDetallePedidoRepository;
        this.vistaPedidoTotalesRepository = vistaPedidoTotalesRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.vistaPedidosAdminRepository = vistaPedidosAdminRepository;
        this.direccionRepository = direccionRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    @Override
    public CrearPedidoResponseDTO crearPedidoDesdeCarrito(CrearPedidoRequestDTO datosCrearPedido) {
        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security
        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que el usuario tenga un carrito creado
        Carrito carritoUsuario = usuario.getCarrito();

        // Si el carrito del usuario está vacío, lanzamos una excepción
        if (carritoUsuario.getDetalleCarrito().isEmpty()) {
            throw new CarritoVacioException("El carrito de compras está vacío. No se puede crear un pedido.");
        }

        // Si el carrito tiene contenido, creamos el pedido.
        pedidoRepository.crearPedidoDesdeCarrito(
                usuario.getIdUsuario(),
                datosCrearPedido.idDireccion(),
                datosCrearPedido.idMetodoPago(),
                datosCrearPedido.comentarios()
        );

        // Forzamos la ejecución de las operaciones pendientes en la base de datos para asegurar que los datos estén actualizados
        entityManager.flush();
        // Limpiamos el contexto de persistencia para evitar que se devuelvan datos obsoletos en la consulta.
        entityManager.clear();

        // Obtenemos el ultimo pedido creado.
        Pedido pedidoCreado = pedidoRepository.obtenerElUltimoPedidoDelUsuario(usuario.getIdUsuario());

        // Si el pedido creado es nulo, lanzamos una excepción.
        if (pedidoCreado == null) {
            throw new PedidoNotFoundException("No se pudo crear el pedido. Por favor, inténtalo de nuevo.");
        }

        // Notificar al usuario que el pedido se ha creado correctamente.
        notificacionServiceImpl.generarNotificacion(usuario.getIdUsuario(), TipoNotificacionEnum.COMPRA);

        // Devolvemos un DTO con la información del pedido creado.
        return new CrearPedidoResponseDTO(pedidoCreado.getIdPedido(), pedidoCreado.getCodigoPedido(), "Pedido creado exitosamente.");
    }

    @Override
    public List<VistaHistorialPedidosUsuarioDTOId> obtenerPedidosUsuario() {
        // Obtenemos al usuario autenticado
        Usuario usuario = obtenerUsuarioAutenticado();

        // Obtenemos la lista de pedidos y la devolvemos
        return vistaHistorialPedidosUsuarioRepository.findByIdUsuario(usuario.getIdUsuario())
                .stream().map(VistaHistorialPedidosUsuarioDTOId::new).toList();
    }

    @Override
    public PedidoCompletoDTOId obtenerPedidoUsuarioPorId(Long idPedido) {
        // Comprobamos que el pedido exista.
        pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFoundException("El pedido no existe"));

        // Obtenemos al usuario autenticado
        Usuario usuario = obtenerUsuarioAutenticado();

        // Obtenemos la información del pedido
        VistaPedidoTotales pedidoTotales = vistaPedidoTotalesRepository
                .findByIdPedidoAndIdUsuario(idPedido, usuario.getIdUsuario());

        // Obtenemos la información de las líneas del pedido
        List<VistaDetallePedidoDTOId> lineas = vistaDetallePedidoRepository.findByIdPedido(idPedido)
                .stream().map(VistaDetallePedidoDTOId::new).toList();

        return new PedidoCompletoDTOId(pedidoTotales, lineas);
    }

    @Override
    public PedidoCompletoDTOId obtenerPedidoEnAdminPorId(Long idPedido) {
        // Comprobamos que el pedido exista.
        pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFoundException("El pedido no existe"));

        // Obtenemos la información del pedido
        VistaPedidoTotales pedidoTotales = vistaPedidoTotalesRepository
                .findByIdPedido(idPedido);

        // Obtenemos la información de las líneas del pedido
        List<VistaDetallePedidoDTOId> lineas = vistaDetallePedidoRepository.findByIdPedido(idPedido)
                .stream().map(VistaDetallePedidoDTOId::new).toList();

        return new PedidoCompletoDTOId(pedidoTotales, lineas);
    }

    // ADMIN

    @Override
    public List<VistaPedidosAdminDTOId> getAllPedidosAdmin(Long idPedido, String codigoPedido, String usuario, LocalDateTime fechaPedido, EstadoPedidoEnum estadoPedido, EstadoPagoEnum estadoPago, String metodoPago) {
        // Inicializar la especificación como null para construirla dinámicamente
        Specification<VistaPedidosAdmin> spec = Specification.anyOf();

        // Agregar filtros a la especificación según los parámetros de búsqueda
        if (idPedido != null && idPedido > 0) {
            spec = spec.and(PedidosAdminSpecification.busquedaPorIdPedido(idPedido));
        }
        if (codigoPedido != null && !codigoPedido.isEmpty()) {
            spec = spec.and(PedidosAdminSpecification.busquedaPorCodigoPedido(codigoPedido));
        }
        if (usuario != null && !usuario.isEmpty()) {
            spec = spec.and(PedidosAdminSpecification.busquedaContiene(usuario));
        }
        if (fechaPedido != null) {
            spec = spec.and(PedidosAdminSpecification.busquedaPorFechaPedido(fechaPedido));
        }
        if (estadoPedido != null) {
            spec = spec.and(PedidosAdminSpecification.busquedaPorEstadoPedido(estadoPedido));
        }
        if (estadoPago != null) {
            spec = spec.and(PedidosAdminSpecification.busquedaPorEstadoPago(estadoPago));
        }
        if (metodoPago != null && !metodoPago.isEmpty()) {
            spec = spec.and(PedidosAdminSpecification.busquedaPorMetodoPago(metodoPago));
        }

        return vistaPedidosAdminRepository.findAll(spec)
                .stream().map(VistaPedidosAdminDTOId::new).toList();
    }

    @Transactional
    @Override
    public void crearPedidoParaUsuario(CrearPedidoAdminRequestDTO datosCrearPedido) {
        // 1. Buscamos el usuario, la dirección y el metodo de pago necesarios
        Usuario usuario = usuarioRepository.findById(datosCrearPedido.idUsuario())
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado"));
        Direccion direccion = direccionRepository.findById(datosCrearPedido.idDireccion())
                .orElseThrow(() -> new DireccionNotFoundException("Dirección no encontrada"));
        MetodoPago metodoPago = metodoPagoRepository.findById(datosCrearPedido.idMetodoPago())
                .orElseThrow(() -> new MetodoPagoNotFoundException("Método de pago no encontrado"));

        // 2. Comprobar que la dirección sea del usuario
        if (!Objects.equals(direccion.getUsuario().getIdUsuario(), usuario.getIdUsuario())) {
            throw new NotNotificationOwnerException("La dirección no pertenece al usuario especificado");
        }

        // 3. Creamos el objeto Pedido base
        Pedido nuevoPedido = Pedido.builder()
                .usuario(usuario)
                .fechaPedido(LocalDateTime.now())
                .ultimaModificacion(LocalDateTime.now())
                .estadoPedido(datosCrearPedido.estadoPedido())
                .estadoPago(datosCrearPedido.estadoPago())
                .direccion(direccion)
                .metodoPago(metodoPago)
                .comentarios(datosCrearPedido.comentarios())
                .codigoPedido("PED-" + System.currentTimeMillis())
                .build();

        // 4. Guardamos el pedido base
        pedidoRepository.save(nuevoPedido);
        
        // 5. Creamos y asociamos los detalles del pedido
        for (var productoDTO : datosCrearPedido.productos()) {
            Producto producto = productoRepository.findById(productoDTO.getIdProducto())
                    .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));

            // Comprobamos que el producto esté activo
            if (productoDTO.getCantidad() > producto.getStock()) {
                throw new InsufficientStockException("No hay suficiente stock para el producto: " + producto.getNombre());
            }
            
            DetallePedidoId id = new DetallePedidoId(nuevoPedido.getIdPedido(), producto.getId());
            DetallePedido detalle = DetallePedido.builder()
                    .id(id)
                    .pedido(nuevoPedido)
                    .producto(producto)
                    .precioUnitario(producto.getPrecio())
                    .cantidad(productoDTO.getCantidad())
                    .iva(producto.getIva())
                    .build();
            
            detallePedidoRepository.save(detalle);

            // Actualizamos el stock del producto
            producto.setStock(producto.getStock() - productoDTO.getCantidad());
            productoRepository.save(producto);
        }

        // 6. Notificamos al usuario de la creación del pedido.
        notificacionServiceImpl.generarNotificacion(nuevoPedido.getUsuario().getIdUsuario(), TipoNotificacionEnum.ESTADO_PEDIDO);
    }

    @Transactional
    @Override
    public PedidoCompletoDTOId agregarUnNuevoProductoAlPedido(Long idPedido, AdminAgregarLineaPedidoRequestDTO datosAgregarLineaPedido) {
        // 1. Buscamos el pedido y el producto
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado"));
        Producto producto = productoRepository.findById(datosAgregarLineaPedido.idProducto())
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado"));
        
        // 2. Creamos o actualizamos el DetallePedido
        DetallePedidoId detalleId = new DetallePedidoId(pedido.getIdPedido(), producto.getId());
        DetallePedido detalle = detallePedidoRepository.findById(detalleId).orElse(
                DetallePedido.builder()
                        .id(detalleId)
                        .pedido(pedido)
                        .producto(producto)
                        .iva(producto.getIva())
                        .build()
        );
        
        detalle.setCantidad(detalle.getCantidad() == null ? datosAgregarLineaPedido.cantidad() : detalle.getCantidad() + datosAgregarLineaPedido.cantidad());
        detalle.setPrecioUnitario(producto.getPrecio());
        
        // 3. Guardamos el detalle
        detallePedidoRepository.save(detalle);
        
        // 4. Actualizamos la fecha de modificación del pedido
        pedido.setUltimaModificacion(LocalDateTime.now());
        pedidoRepository.save(pedido);

        // Sincronizamos y limpiamos para asegurar coherencia al leer desde vistas
        entityManager.flush();
        entityManager.clear();
        
        // 5. Retornamos el pedido actualizado
        return obtenerPedidoUsuarioPorId(pedido.getIdPedido());
    }

    @Transactional
    @Override
    public PedidoCompletoDTOId actualizarDatosPedido(Long idPedido, AdminUpdatePedidoRequestDTO datosActualizarPedido) {
        // 1. Buscamos el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado"));
        
        // Guardamos el estado anterior para verificar si cambia
        EstadoPedidoEnum estadoAnterior = pedido.getEstadoPedido();
        
        // 2. Actualizamos campos permitidos
        if (datosActualizarPedido.estadoPedido() != null) pedido.setEstadoPedido(datosActualizarPedido.estadoPedido());
        if (datosActualizarPedido.estadoPago() != null) pedido.setEstadoPago(datosActualizarPedido.estadoPago());
        if (datosActualizarPedido.comentarios() != null) pedido.setComentarios(datosActualizarPedido.comentarios());
        pedido.setUltimaModificacion(LocalDateTime.now());
        
        // 3. Guardamos el pedido
        pedidoRepository.save(pedido);
        
        // 4. Si el estado ha cambiado, enviamos notificación
        if (datosActualizarPedido.estadoPedido() != null && !datosActualizarPedido.estadoPedido().equals(estadoAnterior)) {
            notificacionServiceImpl.generarNotificacion(pedido.getUsuario().getIdUsuario(), TipoNotificacionEnum.ESTADO_PEDIDO);
        }
        
        // Sincronizamos y limpiamos para asegurar coherencia al leer desde vistas
        entityManager.flush();
        entityManager.clear();
        
        return obtenerPedidoUsuarioPorId(idPedido);
    }

    @Transactional
    @Override
    public PedidoCompletoDTOId actualizarDatosDetallePedido(Long idPedido, Long idProducto, AdminUpdateProductoPedidoRequestDTO datosActualizarDetallePedido) {
        // 1. Buscamos el detalle del pedido
        DetallePedidoId detalleId = new DetallePedidoId(idPedido, idProducto);
        DetallePedido detalle = detallePedidoRepository.findById(detalleId)
                .orElseThrow(() -> new DetallePedidoNotFoundException("Línea de pedido no encontrada"));

        // 2. Actualizamos los campos permitidos
        if (datosActualizarDetallePedido.cantidad() != null) {
            detalle.setCantidad(datosActualizarDetallePedido.cantidad());
        }
        if (datosActualizarDetallePedido.PrecioUnitario_SinIVA() != null) {
            detalle.setPrecioUnitario(datosActualizarDetallePedido.PrecioUnitario_SinIVA());
        }
        if (datosActualizarDetallePedido.IVA() != null) {
            detalle.setIva(datosActualizarDetallePedido.IVA());
        }

        // 3. Guardamos el detalle actualizado
        detallePedidoRepository.save(detalle);

        // 4. Actualizamos la fecha de modificación del pedido asociado
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado"));
        pedido.setUltimaModificacion(LocalDateTime.now());
        pedidoRepository.save(pedido);

        // Sincronizamos y limpiamos para asegurar coherencia al leer desde vistas
        entityManager.flush();
        entityManager.clear();

        // 5. Retornamos el pedido actualizado
        return obtenerPedidoUsuarioPorId(idPedido);
    }

    @Transactional
    @Override
    public void eliminarDetallePedido(Long idPedido, Long idProducto) {
        // 1. Buscamos el detalle del pedido
        DetallePedidoId detalleId = new DetallePedidoId(idPedido, idProducto);
        DetallePedido detalle = detallePedidoRepository.findById(detalleId)
                .orElseThrow(() -> new DetallePedidoNotFoundException("Línea de pedido no encontrada"));
        
        // 2. Eliminamos la línea
        detallePedidoRepository.delete(detalle);
        
        // 3. Actualizamos la fecha de modificación del pedido
        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado"));
        pedido.setUltimaModificacion(LocalDateTime.now());
        pedidoRepository.save(pedido);
        
        // Sincronizamos y limpiamos para asegurar coherencia al leer desde vistas
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * Metodo auxiliar para obtener el usuario autenticado.
     * @return El usuario autenticado.
     * @throws NullEmailAutenticationException Si el email obtenido del contexto de seguridad es nulo.
     * @throws UsuarioNotFoundException Si no se encuentra un usuario con el email obtenido
     */
    private Usuario obtenerUsuarioAutenticado(){
        // Obtenemos el email del usuario autenticado desde el contexto de seguridad de Spring Security
        final String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Validamos que el email no sea nulo antes de continuar
        if (email == null) throw new NullEmailAutenticationException(
                "El email obtenido es nulo por problemas de autenticación, no se puede obtener las direcciones");

        // Devolvemos al usuario autenticado
        return usuarioRepository.findUsuarioByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException(
                        "No se encontró un usuario con el email del usuario autenticado: " + email));
    }
}
