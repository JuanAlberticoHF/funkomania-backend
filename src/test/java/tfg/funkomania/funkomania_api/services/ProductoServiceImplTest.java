package tfg.funkomania.funkomania_api.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo;
import tfg.funkomania.funkomania_api.persistence.repositories.IVistaProductosCatalogoRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link ProductoServiceImpl}.
 *
 * @version 1.0.0
 * @since 0.2.0
 */
public class ProductoServiceImplTest {
    @Test
    void obtenerTodosProductosCatalogosSinFiltros() {
        tfg.funkomania.funkomania_api.persistence.repositories.IVistaProductosCatalogoRepository repo = Mockito.mock(tfg.funkomania.funkomania_api.persistence.repositories.IVistaProductosCatalogoRepository.class);

        tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo v = new tfg.funkomania.funkomania_api.persistence.entities.VistaProductosCatalogo(
                1L,
                "P",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(12),
                false,
                BigDecimal.ZERO,
                null,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(12),
                BigDecimal.valueOf(21),
                5,
                "img",
                "desc",
                true,
                1L,
                "Cat",
                "Padre"
        );

        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(repo.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(List.of(v), pageable, 1));

        ProductoServiceImpl service = new ProductoServiceImpl(repo);
        Page<tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId> page = service.getAllProductos(null, null, null, null, null, pageable);
        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void obtenerTodosProductosCatalogosConFiltrosBusqueda () {
        IVistaProductosCatalogoRepository repo = Mockito.mock(IVistaProductosCatalogoRepository.class);
        VistaProductosCatalogo v = new VistaProductosCatalogo(
                1L,
                "Alpha",
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(12),
                false,
                BigDecimal.ZERO,
                null,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(12),
                BigDecimal.valueOf(21),
                5,
                "img",
                "desc",
                true,
                1L,
                "Cat",
                "Padre"
        );

        Pageable pageable = PageRequest.of(0,20);
        Mockito.when(repo.findAll(Mockito.<Specification<VistaProductosCatalogo>>any(), Mockito.eq(pageable))).thenReturn(new PageImpl<>(List.of(v), pageable, 1));

        ProductoServiceImpl service = new ProductoServiceImpl(repo);
        Page<tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId> page = service.getAllProductos("Alpha", null, null, null, null, pageable);
        assertEquals(1, page.getTotalElements());
    }

    @Test
    void obtenerTodosProductosCatalogosConFiltrosCategoria () {
        tfg.funkomania.funkomania_api.persistence.repositories.IVistaProductosCatalogoRepository repo = Mockito.mock(tfg.funkomania.funkomania_api.persistence.repositories.IVistaProductosCatalogoRepository.class);
        Pageable pageable = PageRequest.of(0,20);
        Mockito.when(repo.findAll(Mockito.<Specification<VistaProductosCatalogo>>any(), Mockito.eq(pageable))).thenReturn(new PageImpl<>(List.of(), pageable, 0));

        ProductoServiceImpl service = new ProductoServiceImpl(repo);
        Page<tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId> page = service.getAllProductos(null, 1L, null, null, null, pageable);
        assertNotNull(page);
    }

    // Tests para ofertas
    @Test
    void obtenerTodosProductosCatalogosConFiltrosOferta () {
        tfg.funkomania.funkomania_api.persistence.repositories.IVistaProductosCatalogoRepository repo = Mockito.mock(tfg.funkomania.funkomania_api.persistence.repositories.IVistaProductosCatalogoRepository.class);
        Pageable pageable = PageRequest.of(0,20);
        Mockito.when(repo.findAllEnOfertaVigenteYActivo(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(List.of(), pageable, 0));

        ProductoServiceImpl service = new ProductoServiceImpl(repo);
        Page<tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId> page = service.getAllProductosEnOfertaActivos(null, null, null, null, pageable);
        assertNotNull(page);
    }
}
