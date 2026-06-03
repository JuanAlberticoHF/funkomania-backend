package tfg.funkomania.funkomania_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;
import tfg.funkomania.funkomania_api.services.ProductoServiceImpl;

/**
 * <p>Controlador REST para manejar las solicitudes relacionadas con los productos.</p>
 * <p>Proporciona un endpoint para obtener todos los productos disponibles en el sistema.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.2.0
 * @since 0.2.0
 */
@RestController
@RequestMapping("/productos")
@Tag(name = "Gestor de Productos", description = "Endpoints para gestionar los productos disponibles en el sistema.")
public class ProductoController {

    /** Servicio productos. */
    private final ProductoServiceImpl productoService;

    public ProductoController(ProductoServiceImpl productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Obtener catalogo de productos", description = "Retorna una lista paginada de todos los productos disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de objetos JSON con los datos de cada producto", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "content": [
                                            {
                                                "activo": true,
                                                "descripcion": "Figura Funko Pop Original de Funkomania",
                                                "descuento": 10.00,
                                                "enOferta": true,
                                                "fechaFinOferta": "2026-12-31T00:00:00",
                                                "id": 1,
                                                "idCategoria": 1,
                                                "imagen": "funko_funkomania.jpg",
                                                "iva": 21.00,
                                                "nombre": "Figura Funkomania",
                                                "nombreCategoria": "Originales",
                                                "nombreCategoriaPadre": null,
                                                "precioFinalConIVA": 16.32,
                                                "precioFinalSinIVA": 13.49,
                                                "precioOriginalConIVA": 18.14,
                                                "precioOriginalSinIVA": 14.99,
                                                "stock": 120
                                            }
                                        ],
                                        "empty": false,
                                        "first": true,
                                        "last": false,
                                        "number": 0,
                                        "numberOfElements": 20,
                                        "pageable": {
                                            "offset": 0,
                                            "pageNumber": 0,
                                            "pageSize": 20,
                                            "paged": true,
                                            "sort": {
                                                "empty": true,
                                                "sorted": false,
                                                "unsorted": true
                                            },
                                            "unpaged": false
                                        },
                                        "size": 20,
                                        "sort": {
                                            "empty": true,
                                            "sorted": false,
                                            "unsorted": true
                                        },
                                        "totalElements": 31,
                                        "totalPages": 2
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "400", description = "Los parámetros de la solicitud no son validos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @GetMapping("/")
    public ResponseEntity<Page<VistaProductosCatalogoDTOId>> getAllProductos(
            @ParameterObject @PageableDefault(size = 20) Pageable pageable,
            @Parameter(
                    description = "Texto para buscar por nombre o descripción",
                    example = "vibranium"
            )
            @RequestParam(required = false) String search,
            @Parameter(
                    description = "ID de la categoría",
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64", minimum = "1")
            )
            @RequestParam(required = false) Long idCategoria,
            @Parameter(
                    description = "Precio mínimo del producto",
                    example = "10.0",
                    schema = @Schema(type = "number", format = "double", minimum = "0")
            )
            @RequestParam(required = false) Double precioMin,
            @Parameter(
                    description = "Precio máximo del producto",
                    example = "50.0",
                    schema = @Schema(type = "number", format = "double", minimum = "0")
            )
            @RequestParam(required = false) Double precioMax,
            @Parameter(
                    description = "Booleano de oferta",
                    example = "true",
                    schema = @Schema(type = "boolean")
            )
            @RequestParam(required = false) Boolean oferta
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.getAllProductos(
                search, idCategoria, precioMin, precioMax, oferta, pageable));
    }

    @Operation(summary = "Obtener catalogo de productos ofertados y activos", description = "Retorna una lista paginada de todos los productos que están en oferta y activos en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de objetos JSON con los datos de cada producto", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "content": [
                                            {
                                                "activo": true,
                                                "descripcion": "Figura Funko Pop Original de Funkomania",
                                                "descuento": 10.00,
                                                "enOferta": true,
                                                "fechaFinOferta": "2026-12-31T00:00:00",
                                                "id": 1,
                                                "idCategoria": 1,
                                                "imagen": "funko_funkomania.jpg",
                                                "iva": 21.00,
                                                "nombre": "Figura Funkomania",
                                                "nombreCategoria": "Originales",
                                                "nombreCategoriaPadre": null,
                                                "precioFinalConIVA": 16.32,
                                                "precioFinalSinIVA": 13.49,
                                                "precioOriginalConIVA": 18.14,
                                                "precioOriginalSinIVA": 14.99,
                                                "stock": 120
                                            }
                                        ],
                                        "empty": false,
                                        "first": true,
                                        "last": false,
                                        "number": 0,
                                        "numberOfElements": 20,
                                        "pageable": {
                                            "offset": 0,
                                            "pageNumber": 0,
                                            "pageSize": 20,
                                            "paged": true,
                                            "sort": {
                                                "empty": true,
                                                "sorted": false,
                                                "unsorted": true
                                            },
                                            "unpaged": false
                                        },
                                        "size": 20,
                                        "sort": {
                                            "empty": true,
                                            "sorted": false,
                                            "unsorted": true
                                        },
                                        "totalElements": 31,
                                        "totalPages": 2
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "400", description = "Los parámetros de la solicitud no son validos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @GetMapping("/ofertas")
    public ResponseEntity<Page<VistaProductosCatalogoDTOId>> getAllProductosOfertas(
            @ParameterObject @PageableDefault(size = 20) Pageable pageable,
            @Parameter(
                    description = "Texto para buscar por nombre o descripción",
                    example = "vibranium"
            )
            @RequestParam(required = false) String search,
            @Parameter(
                    description = "ID de la categoría",
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64", minimum = "1")
            )
            @RequestParam(required = false) Long idCategoria,
            @Parameter(
                    description = "Precio mínimo del producto",
                    example = "10.0",
                    schema = @Schema(type = "number", format = "double", minimum = "0")
            )
            @RequestParam(required = false) Double precioMin,
            @Parameter(
                    description = "Precio máximo del producto",
                    example = "50.0",
                    schema = @Schema(type = "number", format = "double", minimum = "0")
            )
            @RequestParam(required = false) Double precioMax
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.getAllProductosEnOfertaActivos(
                search, idCategoria, precioMin, precioMax, pageable));
    }

    @Operation(summary = "Obtener un producto por su identificador.", description = "Retorna un producto específico del catálogo utilizando su identificador. El producto debe existir en el sistema para ser retornado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Objeto JSON con los datos del producto solicitado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaProductosCatalogoDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                        "activo": true,
                                        "descripcion": "Figura Funko Pop Original de Funkomania",
                                        "descuento": 10.00,
                                        "enOferta": true,
                                        "fechaFinOferta": "2026-12-31T00:00:00",
                                        "id": 1,
                                        "idCategoria": 1,
                                        "imagen": "funko_funkomania.jpg",
                                        "iva": 21.00,
                                        "nombre": "Figura Funkomania",
                                        "nombreCategoria": "Originales",
                                        "nombreCategoriaPadre": null,
                                        "precioFinalConIVA": 16.32,
                                        "precioFinalSinIVA": 13.49,
                                        "precioOriginalConIVA": 18.14,
                                        "precioOriginalSinIVA": 14.99,
                                        "stock": 120
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "400", description = "Los parámetros de la solicitud no son validos.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "El id proporcionado no corresponde a ningún producto existente.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @GetMapping("/{id}")
    public ResponseEntity<VistaProductosCatalogoDTOId> getProductoById(
            @Parameter(
                    description = "Identificador del producto a obtener.",
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64", minimum = "1")
            )
            @PathVariable long id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.getProductoById(id));
    }
}
