package tfg.funkomania.funkomania_api.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ICategoriaRepositoryTest {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Test
    void tieneProductosAsociados_deberiaRetornarFalseCuandoNoTieneProductos() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Nueva Categoria");
        categoria = categoriaRepository.save(categoria);

        boolean tieneProductos = categoriaRepository.tieneProductosAsociados(categoria.getId());

        assertThat(tieneProductos).isFalse();
    }
}
