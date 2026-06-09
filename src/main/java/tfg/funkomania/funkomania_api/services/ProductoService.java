package tfg.funkomania.funkomania_api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.ProductoDTOIdCategoria;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;

import java.util.List;

/**
 * Interfaz de servicio de Producto.
 * Define los métodos para gestionar los productos en el catálogo.
 *
 * @author JuanAlbeticoHF
 * @version 1.2.0
 * @since 0.2.0
 */
public interface ProductoService {
    /**
     * Obtiene una lista de productos.
     * @param search El término de búsqueda para filtrar los productos por nombre o descripción.
     * @param idCategoria El ID de la categoría para filtrar los productos por categoría.
     * @param precioMin El precio mínimo para filtrar los productos por rango de precio.
     * @param precioMax El precio máximo para filtrar los productos por rango de precio.
     * @param oferta Un booleano para filtrar los productos que están en oferta.
     * @param pageable Objeto Pageable que contiene la información de paginación y ordenamiento.
     * @return Una página de objetos VistaProductosCatalogoDTOId que representan los productos disponibles en el catálogo.
     */
    Page<VistaProductosCatalogoDTOId> getAllProductos(String search, Long idCategoria, Double precioMin, Double precioMax, Boolean oferta, Pageable pageable);

    /**
     * Obtiene una lista de productos.
     * @param search El término de búsqueda para filtrar los productos por nombre o descripción.
     * @return Una página de objetos VistaProductosCatalogoDTOId que representan los productos disponibles en el catálogo.
     */
    List<VistaProductosCatalogoDTOId> getAllProductos(String search);

    /**
     * Obtiene una lista de productos en oferta, con paginación y ordenamiento.
     * @param search El término de búsqueda para filtrar los productos por nombre o descripción.
     * @param idCategoria El ID de la categoría para filtrar los productos por categoría.
     * @param precioMin El precio mínimo para filtrar los productos por rango de precio.
     * @param precioMax El precio máximo para filtrar los productos por rango de precio.
     * @param pageable Objeto Pageable que contiene la información de paginación y ordenamiento.
     * @return Una página de objetos VistaProductosCatalogoDTOId que representan los productos en oferta disponibles en el catálogo.
     */
    Page<VistaProductosCatalogoDTOId> getAllProductosEnOfertaActivos(String search, Long idCategoria, Double precioMin, Double precioMax, Pageable pageable);

    /**
     * Obtiene un producto por su ID.
     * @param id El ID del producto que se desea obtener.
     * @return Un objeto Producto que representa el producto con el ID especificado, o lanza excepción si no se encuentra el producto.
     */
    VistaProductosCatalogoDTOId getProductoById(Long id);

    /**
     * Agrega un nuevo producto al catálogo.
     * @param productoDTOIdCategoria Un objeto ProductoDTO que contiene la información del producto a agregar.
     */
    void addProducto(ProductoDTOIdCategoria productoDTOIdCategoria);

    /**
     * Actualiza un producto existente en el catálogo.
     * @param idProducto El ID del producto que se desea actualizar.
     * @param productoDTOIdCategoria Un objeto ProductoDTO que contiene la información actualizada del producto.
     */
    void updateProducto(Long idProducto, ProductoDTOIdCategoria productoDTOIdCategoria);

    /**
     * Elimina un producto del catálogo por su ID.
     * @param idProducto El ID del producto que se desea eliminar.
     */
    void deleteProducto(Long idProducto);
}
