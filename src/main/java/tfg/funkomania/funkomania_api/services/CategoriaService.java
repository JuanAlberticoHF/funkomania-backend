package tfg.funkomania.funkomania_api.services;

import tfg.funkomania.funkomania_api.persistence.entities.Categoria;

import java.util.List;

/**
 * Interfaz de servicio de la entidad Categoria.
 * Define los métodos para realizar operaciones relacionadas con las categorías de productos.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.2.0
 */
public interface CategoriaService {
    /**
     * Obtiene una lista de todas las categorías disponibles en el sistema.
     * @return Una lista de objetos Categoria que representan todas las categorías disponibles en el sistema.
     */
    List<Categoria> getAllCategorias();
}
