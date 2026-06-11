package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tfg.funkomania.funkomania_api.dtos.carrito_dtos.CarritoDTOId_IdProducto;
import tfg.funkomania.funkomania_api.persistence.entities.DetalleCarrito;

/**
 * Interfaz de repositorio para la entidad DetalleCarrito.
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
public interface IDetalleCarritoRepository extends JpaRepository<DetalleCarrito,Long> {
    boolean existsDetalleCarritoByIdCarritoAndProductoId(Long idCarrito, Long productoId);
    boolean deleteDetalleCarritoByIdCarritoAndProductoId(Long idCarrito, Long productoId);
    boolean deleteDetalleCarritoByIdCarrito(Long idCarrito);

    @Modifying
    @Query(value = "CALL sp_agregar_producto_carrito(:p_idUsuario, :p_idProducto, :p_cantidad)", nativeQuery = true)
    CarritoDTOId_IdProducto agregarProductoAlCarrito(@Param("p_idUsuario") Long idUsuario, @Param("p_idProducto") Long idProducto, @Param("p_cantidad") Integer cantidad);
}
