package tfg.funkomania.funkomania_api.services;

import org.springframework.stereotype.Service;
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
}
