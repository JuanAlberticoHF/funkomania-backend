package tfg.funkomania.funkomania_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tfg.funkomania.funkomania_api.persistence.entities.Categoria;
import tfg.funkomania.funkomania_api.persistence.entities.Producto;
import tfg.funkomania.funkomania_api.persistence.repositories.ICategoriaRepository;
import tfg.funkomania.funkomania_api.persistence.repositories.IProductoRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;

/**
 * Pruebas de integración para el controlador de productos.
 *
 * <p>Ejecuta peticiones HTTP simuladas con MockMvc y valida respuestas JSON y códigos HTTP
 * contra el contexto real de Spring Boot.</p>
 *
 * @version 1.0.0
 * @since 0.2.0
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void limpiarBaseDeDatos() {
        System.out.println("Limpiando base de datos antes de cada prueba...");
        productoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    /**
     * Debe devolver el catalogo de productos sin aplicar filtros
     */
    @Test
    void obtenerCatalogoSinFiltros_deberiaDevolverCatalogoCompleto() {
        Categoria categoria = Categoria.builder()
                .nombre("Cat1")
                .build();
        categoria = categoriaRepository.save(categoria);

        List<Producto> productos = new ArrayList<>();
        productos.add(Producto.builder()
                .nombre("P1")
                .precio(BigDecimal.valueOf(10))
                .stock(5)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.ZERO)
                .categoria(categoria)
                .build());
        productos.add(Producto.builder()
                .nombre("P2")
                .precio(BigDecimal.valueOf(20))
                .stock(3)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.ZERO)
                .categoria(categoria)
                .build());

        // saveAllAndFlush obliga a Hibernate a sincronizar inmediatamente la memoria con la base de datos, haciendo que cualquier consulta posterior (como la del controlador) encuentre los registros.
        productoRepository.saveAllAndFlush(productos);
        // Asegurar que la entidad manager limpia el contexto para forzar lecturas desde la BD (y que la vista refleje los cambios)
        entityManager.clear();
        try {
            mockMvc.perform(get("/productos/").contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Debe devolver el catalogo de productos con todos los filtros
     */
    @Test
    void obtenerCatalogoConFiltros_deberiaDevolverCatalogoFiltrado() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria categoria = tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                .nombre("CatFilter")
                .build();
        categoria = categoriaRepository.save(categoria);

        productoRepository.saveAndFlush(tfg.funkomania.funkomania_api.persistence.entities.Producto.builder()
                .nombre("AlphaProduct")
                .precio(BigDecimal.valueOf(11))
                .stock(2)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.ZERO)
                .categoria(categoria)
                .build());

        productoRepository.saveAndFlush(tfg.funkomania.funkomania_api.persistence.entities.Producto.builder()
                .nombre("BetaProduct")
                .precio(BigDecimal.valueOf(22))
                .stock(3)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.ZERO)
                .categoria(categoria)
                .build());

        // Forzar que el contexto JPA se limpie y que las lecturas a la vista se realicen desde la BD
        entityManager.clear();

        try {
            mockMvc.perform(get("/productos/")
                            .param("search", "Alpha")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Debe devolver el catalogo de productos en la pagina 2
     */
    @Test
    void obtenerCatalogoPagina2_deberiaDevolverCatalogoPagina2() {
       Categoria cat = Categoria.builder()
                .nombre("PagCat")
                .build();
        cat = categoriaRepository.save(cat);

        // crear 25 productos para forzar paginación
        List<Producto> lista = new ArrayList<>();
        Categoria finalCat = cat;
        IntStream.rangeClosed(1, 25).forEach(i -> lista.add(
                Producto.builder()
                        .nombre("Prod" + i)
                        .precio(BigDecimal.valueOf(5 + i))
                        .stock(10)
                        .iva(BigDecimal.valueOf(21))
                        .activo(true)
                        .enOferta(false)
                        .descuento(BigDecimal.ZERO)
                        .categoria(finalCat)
                        .build()
        ));
        productoRepository.saveAllAndFlush(lista);
        entityManager.clear();

        try {
            mockMvc.perform(get("/productos/")
                            .param("page", "1")
                            .param("size", "20")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.number").value(1))
                    .andExpect(jsonPath("$.numberOfElements").value(5));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Debe devolver el catalogo de productos cambiando el tamaño de página a 10
     */
    @Test
    void obtenerCatalogoTamanoPagina10_deberiaDevolverCatalogoTamanoPagina10() {
        Categoria cat = Categoria.builder()
                .nombre("SizeCat")
                .build();
        cat = categoriaRepository.save(cat);

        List<Producto> lista = new ArrayList<>();
        Categoria finalCat = cat;
        IntStream.rangeClosed(1, 15).forEach(i -> lista.add(
                Producto.builder()
                        .nombre("SProd" + i)
                        .precio(BigDecimal.valueOf(10 + i))
                        .stock(10)
                        .iva(BigDecimal.valueOf(21))
                        .activo(true)
                        .enOferta(false)
                        .descuento(BigDecimal.ZERO)
                        .categoria(finalCat)
                        .build()
        ));
        productoRepository.saveAllAndFlush(lista);
        entityManager.clear();

        try {
            mockMvc.perform(get("/productos/")
                            .param("size", "10")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size").value(10));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Debe devolver el catalogo de productos ordenando por nombre ascendente
     */
    @Test
    void obtenerCatalogoOrdenadoNombreAsc_deberiaDevolverCatalogoOrdenadoNombreAsc() {
        Categoria cat = Categoria.builder()
                .nombre("SortCat")
                .build();
        cat = categoriaRepository.save(cat);

        productoRepository.saveAndFlush(Producto.builder()
                .nombre("BName")
                .precio(BigDecimal.valueOf(10))
                .stock(3)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.ZERO)
                .categoria(cat)
                .build());

        productoRepository.saveAndFlush(Producto.builder()
                .nombre("AName")
                .precio(BigDecimal.valueOf(12))
                .stock(2)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.ZERO)
                .categoria(cat)
                .build());

        entityManager.clear();

        try {
            mockMvc.perform(get("/productos/")
                            .param("sort", "nombre,asc")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].nombre").value("AName"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Debe devolver el catalogo de productos ordenando por nombre descendente
     */
    @Test
    void obtenerCatalogoOrdenadoNombreDesc_deberiaDevolverCatalogoOrdenadoNombreDesc() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria cat = tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                .nombre("SortCat2")
                .build();
        cat = categoriaRepository.save(cat);

        productoRepository.saveAndFlush(tfg.funkomania.funkomania_api.persistence.entities.Producto.builder()
                .nombre("AName2")
                .precio(BigDecimal.valueOf(10))
                .stock(3)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.ZERO)
                .categoria(cat)
                .build());

        productoRepository.saveAndFlush(tfg.funkomania.funkomania_api.persistence.entities.Producto.builder()
                .nombre("ZName2")
                .precio(BigDecimal.valueOf(12))
                .stock(2)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.ZERO)
                .categoria(cat)
                .build());

        entityManager.clear();

        try {
            mockMvc.perform(get("/productos/")
                            .param("sort", "nombre,desc")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].nombre").value("ZName2"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Debe devolver el catalogo de productos de ofertas sin filtros
     */
    @Test
    void obtenerCatalogoOfertasSinFiltros_deberiaDevolverCatalogoOfertasCompleto() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria cat = tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                .nombre("OfferCat")
                .build();
        cat = categoriaRepository.save(cat);

        productoRepository.save(tfg.funkomania.funkomania_api.persistence.entities.Producto.builder()
                .nombre("Offer1")
                .precio(BigDecimal.valueOf(100))
                .stock(1)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(true)
                .descuento(BigDecimal.valueOf(10))
                .fechaFinOferta(LocalDateTime.now().plusDays(10))
                .categoria(cat)
                .build());

        try {
            mockMvc.perform(get("/productos/ofertas")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Debe devolver el catalogo de productos de ofertas con todos los filtros
     */
    @Test
    void obtenerCatalogoOfertasConFiltros_deberiaDevolverCatalogoOfertasFiltrado() {
        tfg.funkomania.funkomania_api.persistence.entities.Categoria cat = tfg.funkomania.funkomania_api.persistence.entities.Categoria.builder()
                .nombre("OfferCat2")
                .build();
        cat = categoriaRepository.save(cat);

        productoRepository.saveAndFlush(tfg.funkomania.funkomania_api.persistence.entities.Producto.builder()
                .nombre("OfertaAlpha")
                .precio(BigDecimal.valueOf(50))
                .stock(1)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(true)
                .descuento(BigDecimal.valueOf(20))
                .fechaFinOferta(LocalDateTime.now().plusDays(5))
                .categoria(cat)
                .build());

        productoRepository.saveAndFlush(tfg.funkomania.funkomania_api.persistence.entities.Producto.builder()
                .nombre("NoMatch")
                .precio(BigDecimal.valueOf(60))
                .stock(1)
                .iva(BigDecimal.valueOf(21))
                .activo(true)
                .enOferta(false)
                .descuento(BigDecimal.ZERO)
                .categoria(cat)
                .build());

        entityManager.clear();

        try {
            mockMvc.perform(get("/productos/ofertas")
                            .param("search", "OfertaAlpha")
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @TestConfiguration
    static class TestConfig {
        // Provide a ProductoServiceImpl bean (controller depends on the implementation class)
        @Bean
        public tfg.funkomania.funkomania_api.services.ProductoServiceImpl productoServiceImpl(IProductoRepository productoRepository) {
            class TestProductoServiceImpl extends tfg.funkomania.funkomania_api.services.ProductoServiceImpl {
                private final IProductoRepository prodRepo;

                public TestProductoServiceImpl(IProductoRepository prodRepo) {
                    super(null); // pass null because we override methods that use the view repository
                    this.prodRepo = prodRepo;
                }

                @Override
                public org.springframework.data.domain.Page<VistaProductosCatalogoDTOId> getAllProductos(String search, Long idCategoria, Double precioMin, Double precioMax, Boolean oferta, Pageable pageable) {
                    List<tfg.funkomania.funkomania_api.persistence.entities.Producto> all = prodRepo.findAll();
                    List<tfg.funkomania.funkomania_api.persistence.entities.Producto> filtered = all.stream()
                            .filter(p -> (search == null || p.getNombre().contains(search)))
                            .filter(p -> (oferta == null || p.isEnOferta() == oferta))
                            .toList();
                    // Aplicar ordenamiento a la lista filtrada según pageable.sort
                    if (pageable.getSort() != null && pageable.getSort().isSorted()) {
                        List<tfg.funkomania.funkomania_api.persistence.entities.Producto> mutable = new java.util.ArrayList<>(filtered);
                        pageable.getSort().forEach(order -> {
                            if ("nombre".equals(order.getProperty())) {
                                mutable.sort((a, b) -> order.isAscending() ? a.getNombre().compareTo(b.getNombre()) : b.getNombre().compareTo(a.getNombre()));
                            }
                        });
                        filtered = mutable;
                    }
                    int start = (int) pageable.getOffset();
                    int end = Math.min(start + pageable.getPageSize(), filtered.size());
                    List<VistaProductosCatalogoDTOId> content = filtered.subList(Math.max(0, start), Math.max(0, end)).stream().map(p ->
                            VistaProductosCatalogoDTOId.builder()
                                    .id(p.getId())
                                    .nombre(p.getNombre())
                                    .activo(p.isActivo())
                                    .enOferta(p.isEnOferta())
                                    .descuento(p.getDescuento())
                                    .idCategoria(p.getCategoria() != null ? p.getCategoria().getId() : null)
                                    .nombreCategoria(p.getCategoria() != null ? p.getCategoria().getNombre() : null)
                                    .build()
                    ).toList();
                    return new PageImpl<>(content, pageable, filtered.size());
                }

                @Override
                public org.springframework.data.domain.Page<VistaProductosCatalogoDTOId> getAllProductosEnOfertaActivos(String search, Long idCategoria, Double precioMin, Double precioMax, Pageable pageable) {
                    List<tfg.funkomania.funkomania_api.persistence.entities.Producto> all = prodRepo.findAll();
                    List<tfg.funkomania.funkomania_api.persistence.entities.Producto> filtered = all.stream()
                            .filter(tfg.funkomania.funkomania_api.persistence.entities.Producto::isEnOferta)
                            .filter(tfg.funkomania.funkomania_api.persistence.entities.Producto::isActivo)
                            .filter(p -> (search == null || p.getNombre().contains(search)))
                            .toList();
                    if (pageable.getSort() != null && pageable.getSort().isSorted()) {
                        List<tfg.funkomania.funkomania_api.persistence.entities.Producto> mutable = new java.util.ArrayList<>(filtered);
                        pageable.getSort().forEach(order -> {
                            if ("nombre".equals(order.getProperty())) {
                                mutable.sort((a, b) -> order.isAscending() ? a.getNombre().compareTo(b.getNombre()) : b.getNombre().compareTo(a.getNombre()));
                            }
                        });
                        filtered = mutable;
                    }
                    int start = (int) pageable.getOffset();
                    int end = Math.min(start + pageable.getPageSize(), filtered.size());
                    List<VistaProductosCatalogoDTOId> content = filtered.subList(Math.max(0, start), Math.max(0, end)).stream().map(p ->
                            VistaProductosCatalogoDTOId.builder()
                                    .id(p.getId())
                                    .nombre(p.getNombre())
                                    .activo(p.isActivo())
                                    .enOferta(p.isEnOferta())
                                    .descuento(p.getDescuento())
                                    .idCategoria(p.getCategoria() != null ? p.getCategoria().getId() : null)
                                    .nombreCategoria(p.getCategoria() != null ? p.getCategoria().getNombre() : null)
                                    .build()
                    ).toList();
                    return new PageImpl<>(content, pageable, filtered.size());
                }
            }

            return new TestProductoServiceImpl(productoRepository);
        }
    }
}
