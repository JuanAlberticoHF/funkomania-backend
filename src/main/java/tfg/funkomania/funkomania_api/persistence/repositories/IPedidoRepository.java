package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tfg.funkomania.funkomania_api.persistence.entities.Pedido;

/**
 * Interfaz de repositorio para la entidad Pedido.
 *
 * @author JuanAlbeticoHF
 * @version 1.1.0
 * @since 0.7.0
 */
public interface IPedidoRepository extends JpaRepository<Pedido, Long> {
    @Modifying
    @Query(value = "CALL sp_crear_pedido_desde_carrito(:p_idUsuario, :p_idDireccion, :p_idMetodoPago, :p_comentarios)", nativeQuery = true)
    void crearPedidoDesdeCarrito(@Param("p_idUsuario") Long idUsuario,
                                  @Param("p_idDireccion") Long idDireccion,
                                  @Param("p_idMetodoPago") Long idMetodoPago,
                                  @Param("p_comentarios") String comentarios);

    @Query(value = "SELECT p FROM Pedido p WHERE p.usuario.idUsuario = :p_idUsuario order by p.fechaPedido desc limit 1")
    Pedido obtenerElUltimoPedidoDelUsuario(@Param("p_idUsuario") Long idUsuario);

    @Modifying
    @Query(value = "CALL sp_cancelar_pedido(:p_idPedido)", nativeQuery = true)
    void cancelarPedido(@Param("p_idPedido") Long idPedido);
}
