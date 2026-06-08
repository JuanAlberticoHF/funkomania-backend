package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;

import java.util.Optional;

/**
 * Interfaz de repositorio para la entidad Categoria.
 *
 * @author JuanAlbeticoHF
 * @version 0.3.0
 * @since 0.2.0
 */
public interface ICategoriaRepository extends JpaRepository<Categoria,Long> {
    /**
     * Busca una categoria con sus productos asociados
     * @param id identificador de la categoría a buscar.
     * @return un Optional que contiene la categoría encontrada con sus productos asociados, o vacío si no se encuentra.
     */
    @Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.productosAsociados WHERE c.id = :id")
    Optional<Categoria> findCategoriaByIdConProductosParaAdmin(@Param("id") Long id);

    /**
     * Consulta personalizada para verificar si una categoría tiene productos asociados.
     *
     * @param id El ID de la categoría a verificar.
     * @return true si la categoría tiene productos asociados, false en caso contrario.
     */
    @Query("SELECT COUNT(p) > 0 FROM Producto p WHERE p.categoria.id = :id")
    boolean tieneProductosAsociados(@Param("id") Long id);
}
