package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.producto_dtos.ProductoDTOId;

import java.util.List;

/**
 * Interfaz de servicio de la tabla intermedia Lista_Deseos
 * Define los métodos para realizar operaciones de la lista de deseos del usuario
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
public interface ListaDeseosService {
    /**
     * Obtiene la lista de deseos del usuario autenticado.
     * @return Lista de productos en la lista de deseos del usuario.
     */
    List<ProductoDTOId> obtenerListaDeseosDelUsuario();

    /**
     * Agrega un producto a la lista de deseos del usuario autenticado.
     * @param idProducto ID del producto a agregar a la lista de deseos.
     */
    void agregarProductoListaDeseosDelUsuario(Long idProducto);

    /**
     * Elimina un producto de la lista de deseos del usuario autenticado.
     * @param idProducto ID del producto a eliminar de la lista de deseos.
     */
    void eliminarProductoListaDeseosDelUsuario(Long idProducto);
}
