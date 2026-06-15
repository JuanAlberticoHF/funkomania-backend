package tfg.funkomania.funkomania_api.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.persistence.entities.Carrito;
import tfg.funkomania.funkomania_api.persistence.entities.DetalleCarrito;
import tfg.funkomania.funkomania_api.persistence.entities.DetalleCarritoId;
import tfg.funkomania.funkomania_api.persistence.entities.Producto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class IDetalleCarritoRepositoryTest {

    @Autowired
    private IDetalleCarritoRepository detalleCarritoRepository;
    @Autowired
    private ICarritoRepository carritoRepository;
    @Autowired
    private IProductoRepository productoRepository;

    @Test
    void deleteDetalleCarritosByCarrito_deberiaEliminarDetalles() {
        Carrito carrito = carritoRepository.save(new Carrito());
        Producto producto = productoRepository.save(new Producto());
        
        DetalleCarrito detalle = new DetalleCarrito();
        detalle.setId(new DetalleCarritoId(carrito.getIdCarrito(), producto.getId()));
        detalle.setCarrito(carrito);
        detalle.setProducto(producto);
        detalle.setCantidad(1);
        detalleCarritoRepository.save(detalle);

        detalleCarritoRepository.deleteDetalleCarritosByCarrito(carrito);

        assertThat(detalleCarritoRepository.count()).isZero();
    }
}
