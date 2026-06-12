package tfg.funkomania.funkomania_api.controllers.admin_controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.ProductoDTOIdCategoria;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;
import tfg.funkomania.funkomania_api.services.ProductoServiceImpl;

import java.util.List;

/**
 * <p>Controlador REST para manejar las solicitudes relacionadas con los productos del catálogo por parte del administrador.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.6.0
 */
@RestController
@RequestMapping("/admin/productos")
@Tag(name = "[ADMIN] Gestor de Productos", description = "Endpoints para gestionar los productos disponibles en el sistema.")
public class ProductoAdminController {

    /** Servicio productos */
    private final ProductoServiceImpl productoService;

    public ProductoAdminController(ProductoServiceImpl productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Obtener listado de productos", description = "Retorna una lista de productos disponibles en el sistema, con la posibilidad de filtrar por nombre o descripción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna un JSON con una lista de productos con los datos de cada producto", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaProductosCatalogoDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    [
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
                                    ]
                                    """
                    )
            )),
            @ApiResponse(responseCode = "400", description = "Los parámetros de la solicitud no son validos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: el usuario no tiene permiso para acceder al recurso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/")
    public ResponseEntity<List<VistaProductosCatalogoDTOId>> getAllProductos(
            @Parameter(
                    description = "Texto para buscar por nombre o descripción (opcional)",
                    example = "Spider"
            )
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.getAllProductos(search));
    }

    @Operation(summary = "Crear un nuevo producto", description = "Permite al administrador crear un nuevo producto en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Los parámetros de la solicitud no son validos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: el usuario no tiene permiso para acceder al recurso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "La Categoria del producto proporcionado no existe.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/")
    public ResponseEntity<Void> addProducto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo producto a agregar",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDTOIdCategoria.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "idCategoria": 1,
                                        "nombre": "Figura Funko Pop Original 2 de Funkomania",
                                        "precio": 14.99,
                                        "stock": 120,
                                        "imagen": "funko_funkomania.jpg",
                                        "descripcion": "Figura Funko Pop Original 2 de Funkomania",
                                        "iva": 21.00,
                                        "activo": true,
                                        "enOferta": true,
                                        "descuento": 10.00,
                                        "fechaFinOferta": "2026-12-31T00:00:00"
                                    }
                                    """
                            )
                    ))
            @Valid @RequestBody ProductoDTOIdCategoria producto) {
        productoService.addProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Actualizar un producto existente", description = "Permite al administrador actualizar los datos de un producto existente en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Los parámetros de la solicitud no son validos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: el usuario no tiene permiso para acceder al recurso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "La Categoria del producto o el producto con el identificador proporcionado no existe.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{idProducto}")
    public ResponseEntity<Void> updateProducto(
            @Parameter(
                    description = "ID del producto a actualizar",
                    required = true
            )
            @Positive @PathVariable Long idProducto,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo producto a agregar",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductoDTOIdCategoria.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "idCategoria": 1,
                                        "nombre": "Figura Funko Pop Original 3 de Funkomania",
                                        "precio": 14.99,
                                        "stock": 120,
                                        "imagen": "funko_funkomania.jpg",
                                        "descripcion": "Figura Funko Pop Original 3 de Funkomania",
                                        "iva": 21.00,
                                        "activo": true,
                                        "enOferta": true,
                                        "descuento": 10.00,
                                        "fechaFinOferta": "2026-12-31T00:00:00"
                                    }
                                    """
                            )
                    ))
            @Valid @RequestBody ProductoDTOIdCategoria producto) {
        productoService.updateProducto(idProducto, producto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Eliminar un producto existente", description = "Permite al administrador eliminar un producto existente en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado (lógicamente) exitosamente"),
            @ApiResponse(responseCode = "400", description = "Los parámetros de la solicitud no son validos", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: el usuario no tiene permiso para acceder al recurso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "El producto con el identificador proporcionado no existe.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "409", description = "El producto con el identificador proporcionado no ha podido ser eliminado logicamente.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> updateProducto(
            @Parameter(
                    description = "ID del producto a eliminar (lógicamente)",
                    required = true
            )
            @Positive @PathVariable Long idProducto) {
        productoService.deleteProducto(idProducto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
