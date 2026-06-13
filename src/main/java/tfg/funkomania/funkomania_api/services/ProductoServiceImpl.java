package tfg.funkomania.funkomania_api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.ProductoDTOIdCategoria;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.CategoriaNotFoundException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.ProductoNoEliminadoException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.ProductoNotFoundException;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;
import tfg.funkomania.funkomania_api.persistence.entities.Producto;
import tfg.funkomania.funkomania_api.persistence.repositories.ICategoriaRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IProductoRepository;
import tfg.funkomania.funkomania_api.persistence.specifications.VistaProductosCatalogoSpecification;
import tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo;
import tfg.funkomania.funkomania_api.persistence.repositories.IVistaProductosCatalogoRepository;

import java.util.List;

/**
 * <p>Servicio para gestionar los productos en el catálogo de Funkomania.</p>
 * <p>Esta clase implementa la interfaz {@link ProductoService} y proporciona la lógica de negocio para las operaciones
 * relacionadas con los productos en el catálogo.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.2.1
 * @since 0.2.0
 */
@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    /** Repositorio para acceder a la vista de productos en el catálogo. */
    private final IVistaProductosCatalogoRepository vistaProductoRepository;

    private final IProductoRepository productoRepository;

    /** Repositorio para acceder a las categorías de productos. */
    private final ICategoriaRepository categoriaRepository;

    public ProductoServiceImpl(IVistaProductosCatalogoRepository vistaProductoRepository,
                               ICategoriaRepository categoriaRepository,
                               IProductoRepository productoRepository) {
        this.vistaProductoRepository = vistaProductoRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public Page<VistaProductosCatalogoDTOId> getAllProductos(
            String search, Long idCategoria, Double precioMin, Double precioMax, Boolean oferta, Pageable pageable) {
        log.info("Obteniendo catálogo de productos paginado.");
        Specification<VistaProductosCatalogo> spec = getVistaProductosCatalogoSpecifications
                (search, idCategoria, precioMin, precioMax, oferta);

        // Ejecutar la consulta con la especificación y mapear los resultados a DTOs
        if (spec != null) {
            return vistaProductoRepository.findAll(spec, pageable)
                    .map(VistaProductosCatalogoDTOId::new);
        } else {
            return vistaProductoRepository.findAll(pageable)
                    .map(VistaProductosCatalogoDTOId::new);
        }
    }

    @Override
    public List<VistaProductosCatalogoDTOId> getAllProductos(String search) {
        log.info("Obteniendo listado de productos.");
        Specification<VistaProductosCatalogo> spec = getVistaProductosCatalogoSpecifications
                (search, null, null, null, null);

        // Ejecutar la consulta con la especificación y mapear los resultados a DTOs
        if (spec != null) {
            return vistaProductoRepository.findAll(spec).stream()
                    .map(VistaProductosCatalogoDTOId::new).toList();
        } else {
            return vistaProductoRepository.findAll().stream()
                    .map(VistaProductosCatalogoDTOId::new).toList();
        }
    }

    @Override
    public Page<VistaProductosCatalogoDTOId> getAllProductosEnOfertaActivos(
            String search, Long idCategoria, Double precioMin, Double precioMax, Pageable pageable) {
        log.info("Obteniendo catálogo de productos en oferta.");
        Specification<VistaProductosCatalogo> spec = getVistaProductosCatalogoSpecifications
                (search, idCategoria, precioMin, precioMax, null);

        // Ejecutar la consulta con la especificación y mapear los resultados a DTOs
        if (spec != null) {
            return vistaProductoRepository.findAllEnOfertaVigenteYActivo(spec, pageable)
                    .map(VistaProductosCatalogoDTOId::new);
        } else {
            return vistaProductoRepository.findAllEnOfertaVigenteYActivo(pageable)
                    .map(VistaProductosCatalogoDTOId::new);
        }
    }

    @Override
    public VistaProductosCatalogoDTOId getProductoById(Long id) {
        log.info("Obteniendo producto con ID: {}.", id);
        return vistaProductoRepository.findById(id)
                .map(VistaProductosCatalogoDTOId::new)
                .orElseThrow(() -> new ProductoNotFoundException("Producto solicitado no encontrado con ID: " + id));
    }

    @Transactional
    @Override
    public void addProducto(ProductoDTOIdCategoria productoDTOIdCategoria) {
        log.info("Creando nuevo producto: {}.", productoDTOIdCategoria.getNombre());
        // Verificar que la categoría existe, la obtenemos y si no lanzamos excepción
        Categoria categoria = categoriaRepository.findById(productoDTOIdCategoria.getIdCategoria()).orElseThrow(
                () -> new CategoriaNotFoundException("Categoría no encontrada con ID: " + productoDTOIdCategoria.getIdCategoria())
        );

        // Crear una nueva entidad Producto a partir del DTO
        Producto productoNuevo = new Producto(productoDTOIdCategoria);
        productoNuevo.setCategoria(categoria); // Asignar la categoría al producto

        // Guardar el nuevo producto en la base de datos
        productoRepository.save(productoNuevo);
    }

    @Transactional
    @Override
    public void updateProducto(Long idProducto, ProductoDTOIdCategoria productoDTOIdCategoria) {
        log.info("Actualizando producto con ID: {}.", idProducto);
        // Verificar si el producto existe
        if (!productoRepository.existsById(idProducto)) {
            throw new ProductoNotFoundException("No se puede actualizar el producto. Producto con ID " + idProducto + " no encontrado.");
        }

        // Verificar que la categoría existe, la obtenemos y si no lanzamos excepción
        Categoria categoria = categoriaRepository.findById(productoDTOIdCategoria.getIdCategoria()).orElseThrow(
                () -> new CategoriaNotFoundException("Categoría no encontrada con ID: " + productoDTOIdCategoria.getIdCategoria())
        );

        // Crear una nueva entidad Producto a partir del DTO
        Producto productoNuevo = new Producto(productoDTOIdCategoria);
        productoNuevo.setId(idProducto);
        productoNuevo.setCategoria(categoria); // Asignar la categoría al producto

        // Guardar el nuevo producto en la base de datos
        productoRepository.save(productoNuevo);
    }

    @Transactional
    @Override
    public void deleteProducto(Long idProducto) {
        log.info("Eliminando producto con ID: {}.", idProducto);
        // Verificar si el producto existe
        if (!productoRepository.existsById(idProducto)) {
            throw new ProductoNotFoundException("No se puede actualizar el producto. Producto con ID " + idProducto + " no encontrado.");
        }

        // Eliminar el producto de la base de datos
        productoRepository.eliminarLogicamenteProductoByIdProducto(idProducto);

        // Verificar que el producto se ha eliminado correctamente
        if (productoRepository.existsByIdAndActivoTrue(idProducto))
            throw new ProductoNoEliminadoException("El producto con ID " + idProducto + " no pudo ser eliminado.");
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
