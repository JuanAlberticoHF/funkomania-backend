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
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.categoria_dtos.CategoriaDTOId;
import tfg.funkomania.funkomania_api.dtos.categoria_dtos.CategoriaDTOIdProductosAsociados;
import tfg.funkomania.funkomania_api.dtos.categoria_dtos.CategoriaDTORequest;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.VistaProductosCatalogoDTOId;
import tfg.funkomania.funkomania_api.services.CategoriaServiceImpl;

import java.util.List;

/**
 * <p>Controlador REST para manejar las solicitudes relacionadas con las categorías de productos para administradores</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.6.0
 */
@RestController
@RequestMapping("/admin/categorias")
@Validated
@Tag(name = "[ADMIN] Gestor de Categorías", description = "Endpoints para gestionar todas las operaciones relacionadas con las categorías de productos. Solo administrador.")
public class CategoriaAdminController {

    /** Servicio categorías. */
    private final CategoriaServiceImpl categoriaService;

    public CategoriaAdminController(CategoriaServiceImpl categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Operation(summary = "Obtener todas las categorías con productos asociados", description = "Retorna una lista de todas las categorías disponibles en el sistema con sus productos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna un JSON con una lista de cada una de las categorías del sistema, con su categoria padre y productos asociados.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CategoriaDTOIdProductosAsociados.class),
                    examples = @ExampleObject(
                            value = """
                                    [
                                        {
                                            "id": 2,
                                            "nombre": "Originales",
                                            "productosAsociados": [
                                                {
                                                    "activo": true,
                                                    "descripcion": "Figura Funko Pop Original de Funkomania",
                                                    "descuento": 10.00,
                                                    "enOferta": true,
                                                    "fechaFinOferta": "2026-12-31T00:00:00",
                                                    "id": 1,
                                                    "idCategoria": 2,
                                                    "imagen": "funko_funkomania.jpg",
                                                    "iva": 21.00,
                                                    "nombre": "Figura Funkomania",
                                                    "nombreCategoria": "Originales",
                                                    "nombreCategoriaPadre": "Fundadores",
                                                    "precioFinalConIVA": 16.32,
                                                    "precioFinalSinIVA": 13.49,
                                                    "precioOriginalConIVA": 18.14,
                                                    "precioOriginalSinIVA": 14.99,
                                                    "stock": 120
                                                }
                                            ],
                                            "categoriaPadre": {
                                                "id": 1,
                                                "nombre": "Fundadores",
                                                "categoriaPadre": {}
                                            }
                                        }
                                    ]
                                    """
                    )
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/")
    public ResponseEntity<List<CategoriaDTOIdProductosAsociados>> getAllCategoriasConProductosAsociados() {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.obtenerListadoCategoriasConProductosAsociados());
    }

    @Operation(summary = "Obtener todas los productos asociados a una categoria", description = "Retorna una lista de productos asociados a una categorías del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna un JSON con una lista de productos asociados a una categoria.", content = @Content(
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
                                            "idCategoria": 2,
                                            "imagen": "funko_funkomania.jpg",
                                            "iva": 21.00,
                                            "nombre": "Figura Funkomania",
                                            "nombreCategoria": "Originales",
                                            "nombreCategoriaPadre": "Fundadores",
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
            @ApiResponse(responseCode = "404", description = "No se encontró la categoría con el ID proporcionado.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{idCategoria}")
    public ResponseEntity<List<VistaProductosCatalogoDTOId>> getProductosAsociadosDeUnaCategorias(
            @Parameter(description = "ID de la categoría para obtener sus productos asociados", example = "2")
            @PathVariable Long idCategoria) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.obtenerProductosAsociadosDeUnaCategoria(idCategoria));
    }
    
    @Operation(summary = "Crear una nueva categoría", description = "Crea una nueva categoría en el sistema con los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró la categoría padre con el ID proporcionado.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/")
    public ResponseEntity<Void> crearNuevaCategoria(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto JSON con los datos de la nueva categoría a crear. El campo 'categoriaPadre' puede ser nulo si 'idCategoriaPadre' es igual a 1",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDTOId.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        {
                                            "nombre": "Categoria Nueva",
                                            "idCategoriaPadre": 1
                                        }
                                    }
                                    """
                            )
                    ))
            @Valid @RequestBody CategoriaDTORequest categoriaDTORequest) {
        categoriaService.crearCategoria(categoriaDTORequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Actualizar una categoría existente", description = "Actualiza una categoría existente en el sistema con los datos proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró la categoría hija o padre con el ID proporcionado.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{idCategoria}")
    public ResponseEntity<Void> actualizarUnaCategoria(
            @Parameter(description = "ID de la categoría a actualizar", required = true, examples = @ExampleObject(value = "10"))
            @Valid @PathVariable Long idCategoria,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto JSON con los datos actualizados de la categoría. El campo 'categoriaPadre' puede ser nulo si 'idCategoriaPadre' es igual a 1",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaDTOId.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        {
                                            "nombre": "Categoria Nueva",
                                            "idCategoriaPadre": 1
                                        }
                                    }
                                    """
                            )
                    ))
            @Valid @RequestBody CategoriaDTORequest categoriaDTORequest) {
        categoriaService.actualizarCategoria(idCategoria, categoriaDTORequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Eliminar una categoría existente", description = "Elimina una categoría existente en el sistema con el id proporcionado en la ruta de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente."),
            @ApiResponse(responseCode = "404", description = "No se encontró la categoría con el ID proporcionado.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar la categoría con productos asociados.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<Void> eliminarUnaCategoria(
            @Parameter(description = "ID de la categoría a eliminar", required = true, examples = @ExampleObject(value = "10"))
            @Valid @PathVariable Long idCategoria) {
        categoriaService.eliminarCategoria(idCategoria);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
