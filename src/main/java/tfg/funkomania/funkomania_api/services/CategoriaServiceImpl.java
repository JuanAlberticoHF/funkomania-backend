package tfg.funkomania.funkomania_api.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.dtos.categoria_dtos.CategoriaDTORequest;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.CategoriaConProductosException;
import tfg.funkomania.funkomania_api.exceptions.custom_exceptions.CategoriaNotFoundException;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;
import tfg.funkomania.funkomania_api.persistence.repositories.ICategoriaRepository;

import java.util.List;

/**
 * <p>Servicio para gestionar las categorías de productos en la aplicación.</p>
 * <p>Esta clase implementa la interfaz {@link CategoriaService} y proporciona la lógica de negocio para las operaciones
 * relacionadas con las categorías.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.2.0
 */
@Service
public class CategoriaServiceImpl implements CategoriaService {

    /** Repositorio de categorías. */
    private final ICategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(ICategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<VistaProductosCatalogoDTOId> obtenerProductosAsociadosDeUnaCategoria(Long idCategoria) {
        return categoriaRepository.findCategoriaByIdConProductosParaAdmin(idCategoria)
                .map(categoria -> categoria.getProductosAsociados().stream()
                        .map(VistaProductosCatalogoDTOId::new)
                        .toList())
                .orElse(List.of());
    }

    @Transactional
    @Override
    public void crearCategoria(CategoriaDTORequest categoriaDTORequest) {
        // Verificar si la categoría padre existe antes de crear la nueva categoría
        Categoria categoriaPadre = null;

        if (categoriaDTORequest.idCategoriaPadre() != null) {
            categoriaPadre = categoriaRepository.findById(categoriaDTORequest.idCategoriaPadre())
                    .orElseThrow(() -> new CategoriaNotFoundException("La categoría padre con ID " + categoriaDTORequest.idCategoriaPadre() + " no existe."));
        }

        // Crear la nueva categoría con la referencia a la categoría padre (si existe)
        categoriaRepository.save(new Categoria(null, categoriaDTORequest.nombre(), categoriaPadre, null));
    }

    @Transactional
    @Override
    public void actualizarCategoria(Long idCategoria, CategoriaDTORequest categoriaDTORequest) {
        // Verificamos que la categoria existe
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new CategoriaNotFoundException("La categoría con ID " + idCategoria + " no existe."));

        // Verificar si la categoría padre existe antes de actualizar la categoría
        Categoria categoriaPadre = null;

        if (categoriaDTORequest.idCategoriaPadre() != null) {
            categoriaPadre = categoriaRepository.findById(categoriaDTORequest.idCategoriaPadre())
                    .orElseThrow(() -> new CategoriaNotFoundException("La categoría padre con ID " + categoriaDTORequest.idCategoriaPadre() + " no existe."));
        }

        // Actualizar los campos de la categoría
        categoria.setNombre(categoriaDTORequest.nombre());
        categoria.setCategoriaPadre(categoriaPadre);

        // Crear la nueva categoría con la referencia a la categoría padre (si existe)
        categoriaRepository.save(categoria);
    }

    @Transactional
    @Override
    public void eliminarCategoria(Long idCategoria) {
        // Verificamos que la categoria existe
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new CategoriaNotFoundException("La categoría con ID " + idCategoria + " no existe."));

        // Verificar si la categoría tiene productos asociados antes de eliminarla
        if (categoriaRepository.tieneProductosAsociados(idCategoria)) {
            throw new CategoriaConProductosException("No se puede eliminar la categoría con ID " + idCategoria + " porque tiene productos asociados.");
        }

        // Eliminar la categoría
        categoriaRepository.delete(categoria);
    }
}
