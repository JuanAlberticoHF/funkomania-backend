package tfg.funkomania.funkomania_api.services;

import jakarta.persistence.EntityManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.*;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.CarritoVacioException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.NullEmailAutenticationException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.PedidoNotFoundException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.UsuarioNotFoundException;
import tfg.funkomania.funkomania_api.persistence.entities.Carrito;
import tfg.funkomania.funkomania_api.persistence.entities.Pedido;
import tfg.funkomania.funkomania_api.persistence.entities.Usuario;
import tfg.funkomania.funkomania_api.persistence.entities.VistaPedidoTotales;
import tfg.funkomania.funkomania_api.persistence.enums.TipoNotificacionEnum;
import tfg.funkomania.funkomania_api.persistence.repositories.*;

import java.util.List;

/**
 * <p>Servicio para gestionar los pedidos de los usuarios.</p>
 * <p>Esta clase implementa la interfaz {@link PedidoService}.</p>
 *
 * @author JuanAlbeticoHF
 * @version 0.3.0
 * @since 0.7.0
 */
@Service
public class PedidoServiceImpl implements PedidoService {

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

    public PedidoServiceImpl(IVistaHistorialPedidosUsuarioRepository vistaHistorialPedidosUsuarioRepository,
                             IUsuarioRepository usuarioRepository,
                             IPedidoRepository pedidoRepository,
                             EntityManager entityManager,
                             NotificacionServiceImpl notificacionServiceImpl,
                             IVistaDetallePedidoRepository vistaDetallePedidoRepository,
                             IVistaPedidoTotalesRepository vistaPedidoTotalesRepository) {
        this.vistaHistorialPedidosUsuarioRepository = vistaHistorialPedidosUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.entityManager = entityManager;
        this.notificacionServiceImpl = notificacionServiceImpl;
        this.vistaDetallePedidoRepository = vistaDetallePedidoRepository;
        this.vistaPedidoTotalesRepository = vistaPedidoTotalesRepository;
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
