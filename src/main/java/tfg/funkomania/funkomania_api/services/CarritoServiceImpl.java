package tfg.funkomania.funkomania_api.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.dtos.carrito_dtos.VistaCarritoContenidoDTOId;
import tfg.funkomania.funkomania_api.dtos.carrito_dtos.VistaCarritoTotalesContenidoDTOId;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.*;
import tfg.funkomania.funkomania_api.persistence.entities.*;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoCarritoEnum;
import tfg.funkomania.funkomania_api.persistence.repositories.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>Servicio para gestionar el carrito de compras de un usuario en la aplicación.</p>
 * <p>Esta clase implementa la interfaz {@link CarritoService}.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public class CarritoServiceImpl implements CarritoService {

    /** Repositorio de operaciones para la entidad Carrito */
    private final ICarritoRepository carritoRepository;

    /** Repositorio de operaciones para la entidad DetalleCarrito */
    private final IDetalleCarritoRepository detalleCarritoRepository;

    /** Repositorio de operaciones para la vista VistaCarritoTotales */
    private final IVistaCarritoContenidoRepository vistaCarritoContenidoRepository;

    /** Repositorio de operaciones para la vista VistaCarritoTotales */
    private final IVistaCarritoTotalesRepository vistaCarritoTotalesRepository;

    /** Repositorio para obtener al usuario */
    private final IUsuarioRepository usuarioRepository;

    /** Repositorio para obtener al producto */
    private final IProductoRepository productoRepository;

    public CarritoServiceImpl(ICarritoRepository carritoRepository,
                              IDetalleCarritoRepository detalleCarritoRepository,
                              IVistaCarritoContenidoRepository vistaCarritoContenidoRepository,
                              IVistaCarritoTotalesRepository vistaCarritoTotalesRepository,
                              IUsuarioRepository usuarioRepository,
                              IProductoRepository productoRepository) {
        this.carritoRepository = carritoRepository;
        this.detalleCarritoRepository = detalleCarritoRepository;
        this.vistaCarritoContenidoRepository = vistaCarritoContenidoRepository;
        this.vistaCarritoTotalesRepository = vistaCarritoTotalesRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    @Override
    public VistaCarritoTotalesContenidoDTOId obtenerCarritoCompletoUsuario() {
        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security
        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que el usuario tenga un carrito creado
        Carrito carritoUsuario = usuario.getCarrito();

        if (usuario.getCarrito() == null) carritoUsuario = crearCarritoParaUsuario(usuario);

        // Devolvemos el carrito completo del usuario
        return obtenerCarritoCompletoDelUsuario(usuario.getIdUsuario(), carritoUsuario.getIdCarrito());
    }

    @Transactional
    @Override
    public VistaCarritoTotalesContenidoDTOId agregarProductoAlCarrito(Long idProducto, Integer cantidad) {
        // Comprobamos primero que el producto exista
        Producto producto = productoRepository.findById(idProducto).orElseThrow(() -> new ProductoNotFoundException(
                "Producto deseado no encontrado"));

        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security
        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que el usuario tenga un carrito creado
        Carrito carritoUsuario = usuario.getCarrito();

        if (usuario.getCarrito() == null) carritoUsuario = crearCarritoParaUsuario(usuario);

        // Comprobamos si el producto ya existe en el carrito del usuario y lo obtenemos, si no existe, lo creamos
        DetalleCarrito detalleCarrito = detalleCarritoRepository.findById(
                new DetalleCarritoId(carritoUsuario.getIdCarrito(), idProducto)).orElse(null);

        // Si el detalle del carrito no existe, lo creamos
        if (detalleCarrito == null) {
            // Creamos nuevo detalle del carrito
            detalleCarritoRepository.agregarProductoAlCarrito(usuario.getIdUsuario(), producto.getId(), cantidad);
        } else {
            // Si el detalle del carrito ya existe, actualizamos la cantidad sumando la nueva cantidad a la existente
            Integer nuevaCantidad = detalleCarrito.getCantidad() + cantidad;

            // Establecemos la nueva cantidad al producto.
            detalleCarrito.setCantidad(nuevaCantidad);

            // Guardamos el detalle del carrito actualizado
            detalleCarritoRepository.save(detalleCarrito);
        }

        // Actualizamos la última fecha de modificación
        carritoRepository.updateFechaActualizacionByIdCarrito(carritoUsuario.getIdCarrito());

        // Devolvemos el carrito completo del usuario con el producto actualizado
        return obtenerCarritoCompletoDelUsuario(usuario.getIdUsuario(), carritoUsuario.getIdCarrito());
    }

    @Transactional
    @Override
    public VistaCarritoTotalesContenidoDTOId actualizarCantidadProducto(Long idProducto, Integer cantidad) {
        // Comprobamos primero que el producto exista
        productoRepository.findById(idProducto).orElseThrow(() -> new ProductoNotFoundException(
                "Producto deseado no encontrado"));

        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security
        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que el usuario tenga un carrito creado
        Carrito carritoUsuario = usuario.getCarrito();

        if (usuario.getCarrito() == null) throw new CarritoNotFoundException("Carrito no encontrado");

        // Comprobamos si el producto ya existe en el carrito del usuario y lo obtenemos
        DetalleCarrito detalleCarrito = detalleCarritoRepository.findById(
                new DetalleCarritoId(carritoUsuario.getIdCarrito(), idProducto)
                ).orElseThrow(() -> new ProductoNotFoundInCarritoException(
                        "El producto con id " + idProducto + " no se encuentra en el carrito del usuario"));

        // Establecemos la nueva cantidad al producto.
        detalleCarrito.setCantidad(cantidad);

        // Guardamos el detalle del carrito actualizado
        detalleCarritoRepository.save(detalleCarrito);

        // Devolvemos el carrito completo del usuario con el producto actualizado
        return obtenerCarritoCompletoDelUsuario(usuario.getIdUsuario(), carritoUsuario.getIdCarrito());
    }

    @Transactional
    @Override
    public VistaCarritoTotalesContenidoDTOId eliminarProductoDelCarrito(Long idProducto) {
        // Comprobamos primero que el producto exista
        productoRepository.findById(idProducto)
                .orElseThrow(() -> new ProductoNotFoundException("Producto deseado no encontrado"));

        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security
        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que el usuario tenga un carrito creado
        Carrito carritoUsuario = usuario.getCarrito();

        if (usuario.getCarrito() == null) throw new CarritoNotFoundException("Carrito no encontrado");

        // Comprobamos si el producto ya existe en el carrito del usuario y lo obtenemos
        // Construimos el identificador del detalle del carrito.
        DetalleCarritoId detalleCarritoId = new DetalleCarritoId(carritoUsuario.getIdCarrito(), idProducto);

        // Si el detalle del carrito no existe, lanzamos excepción
        DetalleCarrito detalleCarrito = detalleCarritoRepository.findById(detalleCarritoId)
                .orElseThrow(() -> new ProductoNotFoundInCarritoException(
                "El producto con id " + idProducto + " no se encuentra en el carrito del usuario"));

        // Eliminamos el detalle del carrito
        detalleCarritoRepository.delete(detalleCarrito);

        // Devolvemos la información actualizada del carrito del usuario sin el producto eliminado
        return obtenerCarritoCompletoDelUsuario(usuario.getIdUsuario(), carritoUsuario.getIdCarrito());
    }

    @Transactional
    @Override
    public VistaCarritoTotalesContenidoDTOId vaciarCarrito() {

        // Obtenemos usuario autenticado desde el contexto de seguridad de Spring Security
        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que el usuario tenga un carrito creado
        Carrito carritoUsuario = usuario.getCarrito();

        if (usuario.getCarrito() == null) throw new CarritoNotFoundException("Carrito no encontrado");

        // Eliminamos todos los elementos del carrito
        detalleCarritoRepository.deleteDetalleCarritosByCarrito(carritoUsuario);

        // Devolvemos la información actualizada del carrito del usuario sin productos
        return obtenerCarritoCompletoDelUsuario(usuario.getIdUsuario(), carritoUsuario.getIdCarrito());
    }

    /**
     * Metodo para obtener el carrito completo del usuario.
     * @param idUsuario Id del usuario del que se desea obtener el carrito completo.
     * @param idCarrito Id del carrito del que se desea obtener el carrito completo.
     * @return VistaCarritoTotalesContenidoDTOId con el carrito completo del usuario.
     */
    private VistaCarritoTotalesContenidoDTOId obtenerCarritoCompletoDelUsuario(Long idUsuario, Long idCarrito) {
        // Obtenemos el contenido del carrito del usuario
        List<VistaCarritoContenidoDTOId> listaContenidoCarrito =
                vistaCarritoContenidoRepository.findVistaCarritoContenidoByIdUsuario(idUsuario)
                        .stream().map(VistaCarritoContenidoDTOId::new).toList();

        // Obtenemos el total del carrito del usuario
        VistaCarritoTotales carritoTotales = vistaCarritoTotalesRepository.findByIdCarrito(idCarrito);

        // Construimos y devolvemos el DTO con el contenido y el total del carrito del usuario
        return new VistaCarritoTotalesContenidoDTOId(listaContenidoCarrito, carritoTotales);
    }

    /**
     * Metodo auxiliar para crear un nuevo carrito para un usuario dado.
     * @param usuario El usuario para el cual se creará el carrito.
     * @return El carrito creado para el usuario.
     */
    private Carrito crearCarritoParaUsuario(Usuario usuario) {
        if (usuario.getCarrito() == null) {
            Carrito nuevoCarrito = Carrito.builder()
                    .idCarrito(null) // El ID se generará automáticamente al guardar el carrito
                    .usuario(usuario)
                    .fechaCreacion(LocalDateTime.now())
                    .fechaActualizacion(null)
                    .estado(EstadoCarritoEnum.ACTIVO).build();

            // Asociamos el nuevo carrito al usuario
            usuario.setCarrito(nuevoCarrito);

            // Guardamos el usuario con el nuevo carrito asociado
            Usuario usuarioGuardado = usuarioRepository.save(usuario);

            // Devolvemos el carrito del usuario guardado, que ahora incluye el nuevo carrito creado
            return usuarioGuardado.getCarrito();
        }
        return usuario.getCarrito();
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
