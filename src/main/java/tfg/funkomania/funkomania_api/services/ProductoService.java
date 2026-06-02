package tfg.funkomania.funkomania_api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;

/**
 * Interfaz de servicio de Producto.
 * Define los métodos para gestionar los productos en el catálogo.
 *
 * @author JuanAlbeticoHF
 * @version 0.1.0
 * @since 0.2.0
 */
public interface ProductoService {
    /**
     * Obtiene una lista de productos.
     * @param pageable Objeto Pageable que contiene la información de paginación y ordenamiento.
     * @return Una página de objetos VistaProductosCatalogoDTOId que representan los productos disponibles en el catálogo.
     */
    Page<VistaProductosCatalogoDTOId> getAllProductos(Pageable pageable);

    /**
     * Obtiene una lista de productos en oferta, con paginación y ordenamiento.
     * @param pageable Objeto Pageable que contiene la información de paginación y ordenamiento.
     * @return Una página de objetos VistaProductosCatalogoDTOId que representan los productos en
     */
    Page<VistaProductosCatalogoDTOId> getAllProductosEnOfertaActivos(Pageable pageable);
}
