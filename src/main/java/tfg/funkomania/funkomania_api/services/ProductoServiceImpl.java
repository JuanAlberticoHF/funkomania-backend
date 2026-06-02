package tfg.funkomania.funkomania_api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;
import tfg.funkomania.funkomania_api.persistence.repositories.IVistaProductosCatalogoRepository;

/**
 * <p>Servicio para gestionar los productos en el catálogo de Funkomania.</p>
 * <p>Esta clase implementa la interfaz {@link ProductoService} y proporciona la lógica de negocio para las operaciones
 * relacionadas con los productos en el catálogo.</p>
 *
 * @author JuanAlbeticoHF
 * @version 0.1.0
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
    public Page<VistaProductosCatalogoDTOId> getAllProductos(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(VistaProductosCatalogoDTOId::new);
    }

    @Override
    public Page<VistaProductosCatalogoDTOId> getAllProductosEnOfertaActivos(Pageable pageable) {
        return productoRepository.findAllEnOfertaVigenteYActivo(pageable)
                .map(VistaProductosCatalogoDTOId::new);
    }
}
