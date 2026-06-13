package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.carrito_dtos.VistaCarritoTotalesContenidoDTOId;

/**
 * Interfaz que define los métodos para gestionar el carrito de compras del usuario en la aplicación.
 *
 * @author JuanAlberticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface CarritoService {
    /**
     * Obtiene el carrito completo del usuario, incluyendo los totales y el contenido.
     * @return Un DTO que contiene el carrito completo del usuario, con totales y contenido.
     */
    VistaCarritoTotalesContenidoDTOId obtenerCarritoCompletoUsuario();

    /**
     * Agrega un producto al carrito del usuario, especificando el ID del producto y la cantidad deseada.
     * @param idProducto El ID del producto que se desea agregar al carrito.
     * @param cantidad La cantidad del producto que se desea agregar al carrito.
     * @return Un DTO que contiene el carrito completo del usuario, con totales y contenido actualizado después de agregar el producto.
     */
    VistaCarritoTotalesContenidoDTOId agregarProductoAlCarrito(Long idProducto, Integer cantidad);

    /**
     * Actualiza la cantidad de un producto específico en el carrito del usuario.
     * @param idProducto El ID del producto cuya cantidad se desea actualizar en el carrito.
     * @param cantidad La nueva cantidad del producto que se desea establecer en el carrito.
     * @return Un DTO que contiene el carrito completo del usuario, con totales y contenido actualizado después de modificar la cantidad del producto.
     */
    VistaCarritoTotalesContenidoDTOId actualizarCantidadProducto(Long idProducto, Integer cantidad);

    /**
     * Elimina un producto específico del carrito del usuario, utilizando el ID del producto para identificar cuál producto se desea eliminar.
     * @param idProducto El ID del producto que se desea eliminar del carrito.
     */
    VistaCarritoTotalesContenidoDTOId eliminarProductoDelCarrito(Long idProducto);

    /**
     * Elimina un producto específico del carrito del usuario, utilizando el ID del producto para identificar cuál producto se desea eliminar.
     */
    VistaCarritoTotalesContenidoDTOId vaciarCarrito();
}
