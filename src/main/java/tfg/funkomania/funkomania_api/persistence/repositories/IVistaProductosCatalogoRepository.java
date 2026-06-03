package tfg.funkomania.funkomania_api.persistence.repositories;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo;

/**
 * Interfaz de repositorio para la entidad VistaProductosCatalogo.
 *
 * @author JuanAlbeticoHF
 * @version 1.1.0
 * @since 0.2.0
 */
public interface IVistaProductosCatalogoRepository extends JpaRepository<VistaProductosCatalogo, Long>, JpaSpecificationExecutor<VistaProductosCatalogo> {
    @NullMarked
    Page<VistaProductosCatalogo> findAll(Pageable pageable);
    @NullMarked
    Page<VistaProductosCatalogo> findAll(Specification<VistaProductosCatalogo> spec, Pageable pageable);

    @Query("SELECT v FROM VistaProductosCatalogo v WHERE v.enOferta = true AND v.descuento > 0 AND (v.fechaFinOferta IS NULL OR v.fechaFinOferta >= CURRENT_TIMESTAMP) AND v.activo = true")
    Page<VistaProductosCatalogo> findAllEnOfertaVigenteYActivo(Pageable pageable);
    @Query("SELECT v FROM VistaProductosCatalogo v WHERE v.enOferta = true AND v.descuento > 0 AND (v.fechaFinOferta IS NULL OR v.fechaFinOferta >= CURRENT_TIMESTAMP) AND v.activo = true")
    Page<VistaProductosCatalogo> findAllEnOfertaVigenteYActivo(Specification<VistaProductosCatalogo> spec, Pageable pageable);
}
