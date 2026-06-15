package tfg.funkomania.funkomania_api.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.persistence.entities.Carrito;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoCarritoEnum;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ICarritoRepositoryTest {

    @Autowired
    private ICarritoRepository carritoRepository;

    @Test
    void updateFechaActualizacionByIdCarrito_deberiaActualizarFecha() {
        Carrito carrito = new Carrito();
        carrito.setFechaCreacion(LocalDateTime.now().minusDays(1));
        carrito.setFechaActualizacion(LocalDateTime.now().minusDays(1));
        carrito.setEstado(EstadoCarritoEnum.ACTIVO);
        carrito = carritoRepository.save(carrito);

        carritoRepository.updateFechaActualizacionByIdCarrito(carrito.getIdCarrito());
        
        Carrito actualizado = carritoRepository.findById(carrito.getIdCarrito()).orElseThrow();
        assertThat(actualizado.getFechaActualizacion()).isAfter(carrito.getFechaCreacion());
    }
}
