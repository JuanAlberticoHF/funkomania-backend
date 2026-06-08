package tfg.funkomania.funkomania_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.ProductoDTOId;
import tfg.funkomania.funkomania_api.services.ListaDeseosServiceImpl;

import java.util.List;

/**
 * <p>Controlador REST para manejar las solicitudes relacionadas con los productos deseados del cliente</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
@RestController
@RequestMapping("/usuario/lista-deseos")
@Tag(name = "Gestion Lista de Deseos", description = "Endpoints para gestionar la lista de deseos del cliente autenticado, incluyendo la obtención de su lista de deseos, la adición de productos a la lista y la eliminación de productos de la lista.")
public class ListaDeseosController {
    /** Servicio de lista de deseos que contiene la lógica de negocio de todas las operaciones */
    private final ListaDeseosServiceImpl listaDeseosService;

    private ListaDeseosController(ListaDeseosServiceImpl listaDeseosService) {
        this.listaDeseosService = listaDeseosService;
    }

    @Operation(summary = "Obtener los productos deseados del cliente", description = "Obtiene la lista de productos que el cliente autenticado ha añadido a su lista de deseos. Requiere que el usuario esté autenticado para acceder a esta información.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de deseos del cliente autenticado obtenida exitosamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductoDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    [
                                      {
                                        "id": 0,
                                        "nombre": "Funkodes",
                                        "precio": 29.99,
                                        "stock": 20,
                                        "imagen": "funko.url",
                                        "descripcion": "Funko deseado",
                                        "iva": 21,
                                        "activo": true,
                                        "enOferta": true,
                                        "descuento": 60,
                                        "fechaFinOferta": "2026-06-08T09:21:21.333Z"
                                      }
                                    ]
                                    """
                    )
            )),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: El usuario no está autenticado o no tiene permisos para acceder a esta información", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontro el usuario autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/")
    public ResponseEntity<List<ProductoDTOId>> obtenerListaDeseosDelUsuario() {
        return ResponseEntity.ok(listaDeseosService.obtenerListaDeseosDelUsuario());
    }

    @Operation(summary = "Añadir un producto a la lista de deseados del usuario", description = "Agrega un producto específico a la lista de deseos del cliente autenticado. Requiere que el usuario esté autenticado para realizar esta acción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto añadido a la lista de deseos del cliente autenticado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: El usuario no está autenticado o no tiene permisos para acceder a esta información", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontro el usuario autenticado o el identificador del producto indicados no existen", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "409", description = "El producto ya se encuentra en la lista de deseados", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{idProducto}")
    public ResponseEntity<Void> addProductoAListaDeseos(@PathVariable Long idProducto) {
        listaDeseosService.agregarProductoListaDeseosDelUsuario(idProducto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Eliminar un producto a la lista de deseados del usuario", description = "Elimina un producto específico de la lista de deseos del cliente autenticado. Requiere que el usuario esté autenticado para realizar esta acción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado de la lista de deseos del cliente autenticado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: El usuario no está autenticado o no tiene permisos para acceder a esta información", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontro el usuario autenticado o el identificador del producto indicados no existen o el producto no se encuentra en la lista de deseados del usuario", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> eliminarProductoDeListaDeseos(@PathVariable Long idProducto) {
        listaDeseosService.eliminarProductoListaDeseosDelUsuario(idProducto);
        return ResponseEntity.ok().build();
    }
}
