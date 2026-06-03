package tfg.funkomania.funkomania_api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tfg.funkomania.funkomania_api.persistence.specifications.VistaProductosCatalogoSpecification;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;
import tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo;
import tfg.funkomania.funkomania_api.persistence.repositories.IVistaProductosCatalogoRepository;

/**
 * <p>Servicio para gestionar los productos en el catálogo de Funkomania.</p>
 * <p>Esta clase implementa la interfaz {@link ProductoService} y proporciona la lógica de negocio para las operaciones
 * relacionadas con los productos en el catálogo.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.2.0
 */
@Service
public class ProductoServiceImpl implements ProductoService {

    /** Repositorio para acceder a la vista de productos en el catálogo. */
    private final IVistaProductosCatalogoRepository productoRepository;

    public ProductoServiceImpl(IVistaProductosCatalogoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Page<VistaProductosCatalogoDTOId> getAllProductos(
            String search, Long idCategoria, Double precioMin, Double precioMax, Boolean oferta, Pageable pageable) {

        Specification<VistaProductosCatalogo> spec = getVistaProductosCatalogoSpecifications
                (search, idCategoria, precioMin, precioMax, oferta);

        // Ejecutar la consulta con la especificación y mapear los resultados a DTOs
        if (spec != null) {
            return productoRepository.findAll(spec, pageable)
                    .map(VistaProductosCatalogoDTOId::new);
        } else {
            return productoRepository.findAll(pageable)
                    .map(VistaProductosCatalogoDTOId::new);
        }
    }

    @Override
    public Page<VistaProductosCatalogoDTOId> getAllProductosEnOfertaActivos(
            String search, Long idCategoria, Double precioMin, Double precioMax, Pageable pageable) {

        Specification<VistaProductosCatalogo> spec = getVistaProductosCatalogoSpecifications
                (search, idCategoria, precioMin, precioMax, null);

        // Ejecutar la consulta con la especificación y mapear los resultados a DTOs
        if (spec != null) {
            return productoRepository.findAllEnOfertaVigenteYActivo(spec, pageable)
                    .map(VistaProductosCatalogoDTOId::new);
        } else {
            return productoRepository.findAllEnOfertaVigenteYActivo(pageable)
                    .map(VistaProductosCatalogoDTOId::new);
        }
    }

    /**
     * Construye una especificación combinando los filtros proporcionados para buscar productos en la vista del catálogo.
     * @param search El término de búsqueda para filtrar los productos por nombre o descripción.
     * @param idCategoria El ID de la categoría para filtrar los productos por categoría.
     * @param precioMin El precio mínimo para filtrar los productos por rango de precio.
     * @param precioMax El precio máximo para filtrar los productos por rango de precio.
     * @param oferta Un booleano para filtrar los productos que están en oferta.
     * @return Una especificación que combina los filtros proporcionados para buscar productos en la vista del catálogo.
     */
    public static Specification<VistaProductosCatalogo> getVistaProductosCatalogoSpecifications(
            String search, Long idCategoria, Double precioMin, Double precioMax, Boolean oferta) {

        // Inicializar la especificación como null para construirla dinámicamente
        Specification<VistaProductosCatalogo> spec = null;

        // Agregar filtros a la especificación según los parámetros de búsqueda
        // - Si se proporciona un término de búsqueda, filtrar por nombre.
        if (search != null && !search.isEmpty()) {
            spec = VistaProductosCatalogoSpecification.busquedaContiene(search);
        }
        // - Si se proporciona un ID de categoría, filtrar por categoría.
        if (idCategoria != null) {
            spec = (spec == null) ? VistaProductosCatalogoSpecification.idCategoriaIgual(idCategoria)
                    : spec.and(VistaProductosCatalogoSpecification.idCategoriaIgual(idCategoria));
        }
        // - Si se proporciona un precio mínimo, filtrar por productos con precio final con IVA mayor o igual al mínimo.
        if (precioMin != null) {
            spec = (spec == null) ? VistaProductosCatalogoSpecification.precioMayorOIgualQue(precioMin)
                    : spec.and(VistaProductosCatalogoSpecification.precioMayorOIgualQue(precioMin));
        }
        // - Si se proporciona un precio máximo, filtrar por productos con precio final con IVA menor o igual al máximo.
        if (precioMax != null) {
            spec = (spec == null) ? VistaProductosCatalogoSpecification.precioMenorOIgualQue(precioMax)
                    : spec.and(VistaProductosCatalogoSpecification.precioMenorOIgualQue(precioMax));
        }
        // - Si se proporciona un valor para oferta, filtrar por productos que estén en oferta o no según el valor booleano.
        if (oferta != null) {
            spec = (spec == null) ? VistaProductosCatalogoSpecification.estaEnOferta(oferta)
                    : spec.and(VistaProductosCatalogoSpecification.estaEnOferta(oferta));
        }

        return spec;
    }
}
