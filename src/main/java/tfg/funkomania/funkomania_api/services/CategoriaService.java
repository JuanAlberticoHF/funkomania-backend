package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.dtos.categoria_dtos.CategoriaDTOIdProductosAsociados;
import tfg.funkomania.funkomania_api.dtos.categoria_dtos.CategoriaDTORequest;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;

import java.util.List;

/**
 * Interfaz de servicio de la entidad Categoria.
 * Define los métodos para realizar operaciones relacionadas con las categorías de productos.
 *
 * @author JuanAlbeticoHF
 * @version 1.2.0
 * @since 0.2.0
 */
public interface CategoriaService {
    /**
     * Obtiene una lista de todas las categorías disponibles en el sistema.
     * @return Una lista de objetos Categoria que representan todas las categorías disponibles en el sistema.
     */
    List<Categoria> getAllCategorias();

    /**
     * Obtiene todas las categorías incluyendo los productos asociados a cada categoría.
     * @return Una lista de Categorías con productos asociados VistaProductosCatalogoDTOId que representan todas las categorías disponibles en el sistema.
     */
    List<CategoriaDTOIdProductosAsociados> obtenerListadoCategoriasConProductosAsociados();

    /**
     * Obtiene una categoría por su ID, incluyendo los productos asociados a esa categoría.
     * @param idCategoria El ID de la categoría que se desea obtener.
     * @return Una lista de objetos VistaProductosCatalogoDTOId que representan los productos asociados a la categoría especificada. Si la categoría no existe, se devuelve una lista vacía.
     */
    List<VistaProductosCatalogoDTOId> obtenerProductosAsociadosDeUnaCategoria(Long idCategoria);

    /**
     * Crea una nueva categoría en el sistema a partir de un objeto CategoriaDTORequest.
     * @param categoriaDTORequest El objeto CategoriaDTORequest que contiene la información de la categoría a crear.
     */
    void crearCategoria(CategoriaDTORequest categoriaDTORequest);

    /**
     * Elimina una categoría del sistema por su ID.
     * @param idCategoria El ID de la categoría que se desea eliminar.
     * @param categoriaDTORequest El objeto CategoriaDTORequest que contiene el ID de la categoría a eliminar.
     */
    void actualizarCategoria(Long idCategoria, CategoriaDTORequest categoriaDTORequest);

    /**
     * Elimina una categoría del sistema por su ID.
     * @param idCategoria El ID de la categoría que se desea eliminar.
     */
    void eliminarCategoria(Long idCategoria);
}
