package tfg.funkomania.funkomania_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.carrito_dtos.VistaCarritoTotalesContenidoDTOId;
import tfg.funkomania.funkomania_api.services.CarritoServiceImpl;

/**
 * <p>Controlador REST para gestionar las operaciones relacionadas con el carrito de compras.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.7.0
 */
@RestController
@RequestMapping("/carrito")
@Tag(name = "Gestor de Carrito", description = "Endpoints para gestionar el carrito de compras del usuario, incluyendo agregar productos, eliminar productos y obtener el contenido del carrito.")
public class CarritoController {

    /** Servicio para gestionar las operaciones del carrito de compras. */
    private final CarritoServiceImpl carritoService;

    public CarritoController(CarritoServiceImpl carritoService) {
        this.carritoService = carritoService;
    }

    @Operation(summary = "Obtener el contenido del carrito de compras", description = "Retorna el contenido actual del carrito de compras del usuario, el listado de productos, sus cantidades, total acumulado y total a pagar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido del carrito obtenido satisfactoriamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaCarritoTotalesContenidoDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "baseImponible": 16.99,
                                      "idCarrito": 2,
                                      "idUsuario": 1,
                                      "items": [
                                        {
                                          "cantidad": 1,
                                          "descuento": 0,
                                          "enOferta": false,
                                          "fechaFinOferta": null,
                                          "idCarrito": 2,
                                          "idProducto": 1,
                                          "idUsuario": 1,
                                          "image": "image_url.png",
                                          "ivaPorcentaje": 21,
                                          "precioOriginalSinIVA": 16.99,
                                          "precioUnitarioConIVA": 20.56,
                                          "precioUnitarioSinIVA": 16.99,
                                          "producto": "Funko Pop Funkomania",
                                          "subtotalPosicion": 20.56
                                        }
                                      ],
                                      "totalAPagar": 20.56,
                                      "totalArticulosDiferentes": 1,
                                      "totalUnidadesFisicas": 1
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontró el usuario autenticado", content = @Content(
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
    public VistaCarritoTotalesContenidoDTOId obtenerCarritoDelUsuario() {
        return carritoService.obtenerCarritoCompletoUsuario();
    }

    @Operation(summary = "Agregar un producto al carrito de compras", description = "Agrega un producto específico al carrito de compras del usuario. Si el producto ya existe en el carrito, se incrementará la cantidad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto agregado al carrito satisfactoriamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaCarritoTotalesContenidoDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "baseImponible": 16.99,
                                      "idCarrito": 2,
                                      "idUsuario": 1,
                                      "items": [
                                        {
                                          "cantidad": 1,
                                          "descuento": 0,
                                          "enOferta": false,
                                          "fechaFinOferta": null,
                                          "idCarrito": 2,
                                          "idProducto": 1,
                                          "idUsuario": 1,
                                          "image": "image_url.png",
                                          "ivaPorcentaje": 21,
                                          "precioOriginalSinIVA": 16.99,
                                          "precioUnitarioConIVA": 20.56,
                                          "precioUnitarioSinIVA": 16.99,
                                          "producto": "Funko Pop Funkomania",
                                          "subtotalPosicion": 20.56
                                        }
                                      ],
                                      "totalAPagar": 20.56,
                                      "totalArticulosDiferentes": 1,
                                      "totalUnidadesFisicas": 1
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontró producto o no se encontró el usuario autenticado", content = @Content(
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
    public VistaCarritoTotalesContenidoDTOId agregarProductoAlCarritoDelUsuario(
            @Parameter(
                    description = "ID del producto a agregar al carrito",
                    required = true,
                    examples = @ExampleObject(value = "1"))
            @PathVariable Long idProducto,
            @Parameter(
                    description = "Cantidad del producto a agregar al carrito (opcional, por defecto es 1)",
                    schema = @Schema(type = "integer", format = "int64", minimum = "1"))
            @RequestParam(required = false) Integer cantidad) {
        return carritoService.agregarProductoAlCarrito(idProducto, cantidad);
    }

    @Operation(summary = "Actualizar la cantidad de un producto en el carrito de compras", description = "Actualiza la cantidad de un producto específico en el carrito de compras del usuario. Si la cantidad se establece en cero, el producto se eliminará del carrito.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad del producto actualizada en el carrito satisfactoriamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaCarritoTotalesContenidoDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "baseImponible": 16.99,
                                      "idCarrito": 2,
                                      "idUsuario": 1,
                                      "items": [
                                        {
                                          "cantidad": 1,
                                          "descuento": 0,
                                          "enOferta": false,
                                          "fechaFinOferta": null,
                                          "idCarrito": 2,
                                          "idProducto": 1,
                                          "idUsuario": 1,
                                          "image": "image_url.png",
                                          "ivaPorcentaje": 21,
                                          "precioOriginalSinIVA": 16.99,
                                          "precioUnitarioConIVA": 20.56,
                                          "precioUnitarioSinIVA": 16.99,
                                          "producto": "Funko Pop Funkomania",
                                          "subtotalPosicion": 20.56
                                        }
                                      ],
                                      "totalAPagar": 20.56,
                                      "totalArticulosDiferentes": 1,
                                      "totalUnidadesFisicas": 1
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontró producto o no se encontró el usuario autenticado o no se encontró el carrito del usuario o el producto no se encuentra en el carrito", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{idProducto}")
    public VistaCarritoTotalesContenidoDTOId actualizarProductoDelCarrito(
            @Parameter(
                    description = "ID del producto a actualizar en el carrito.",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", minimum = "1"),
                    examples = @ExampleObject(value = "1"))
            @PathVariable Long idProducto,
            @Parameter(
                    description = "Cantidad del producto a actualizar en el carrito (opcional, por defecto es 1)",
                    schema = @Schema(type = "integer", format = "int64", minimum = "1"))
            @RequestParam Integer cantidad) {
        return carritoService.actualizarCantidadProducto(idProducto, cantidad);
    }

    @Operation(summary = "Eliminar un producto del carrito de compras", description = "Elimina un producto específico del carrito de compras del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado del carrito satisfactoriamente, devuelve el carrito actualizado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaCarritoTotalesContenidoDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "baseImponible": 16.99,
                                      "idCarrito": 2,
                                      "idUsuario": 1,
                                      "items": [
                                        {
                                          "cantidad": 1,
                                          "descuento": 0,
                                          "enOferta": false,
                                          "fechaFinOferta": null,
                                          "idCarrito": 2,
                                          "idProducto": 1,
                                          "idUsuario": 1,
                                          "image": "image_url.png",
                                          "ivaPorcentaje": 21,
                                          "precioOriginalSinIVA": 16.99,
                                          "precioUnitarioConIVA": 20.56,
                                          "precioUnitarioSinIVA": 16.99,
                                          "producto": "Funko Pop Funkomania",
                                          "subtotalPosicion": 20.56
                                        }
                                      ],
                                      "totalAPagar": 20.56,
                                      "totalArticulosDiferentes": 1,
                                      "totalUnidadesFisicas": 1
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontró producto o no se encontró el usuario autenticado o no se encontró el carrito del usuario o el producto no se encuentra en el carrito", content = @Content(
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
    public VistaCarritoTotalesContenidoDTOId eliminarProductoDelCarrito(
            @Parameter(
                    description = "ID del producto a eliminar del carrito.",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", minimum = "1"),
                    examples = @ExampleObject(value = "1"))
            @PathVariable Long idProducto) {
        return carritoService.eliminarProductoDelCarrito(idProducto);
    }

    @Operation(summary = "Vacía el carrito de compras del usuario", description = "Elimina todos los productos del carrito de compras del usuario, dejando el carrito vacío.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El carrito de compra ha sido vaciado exitosamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaCarritoTotalesContenidoDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                      "baseImponible": 0,
                                      "idCarrito": 2,
                                      "idUsuario": 1,
                                      "items": [],
                                      "totalAPagar": 0,
                                      "totalArticulosDiferentes": 0,
                                      "totalUnidadesFisicas": 0
                                    }
                                    """
                    )
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontró el usuario autenticado o no se encontró el carrito del usuario.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/")
    public VistaCarritoTotalesContenidoDTOId vaciarCarritoDelUsuario() {
        return carritoService.vaciarCarrito();
    }
}
