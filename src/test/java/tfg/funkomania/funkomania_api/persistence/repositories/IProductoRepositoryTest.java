package tfg.funkomania.funkomania_api.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.persistence.entities.Producto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class IProductoRepositoryTest {

    @Autowired
    private IProductoRepository productoRepository;

    @Test
    void eliminarLogicamenteProductoByIdProducto_deberiaDesactivarProducto() {
        Producto producto = new Producto();
        producto.setNombre("Producto");
        producto.setActivo(true);
        producto = productoRepository.save(producto);

        productoRepository.eliminarLogicamenteProductoByIdProducto(producto.getId());

        Producto actualizado = productoRepository.findById(producto.getId()).orElseThrow();
        assertThat(actualizado.isActivo()).isFalse();


    }
}
