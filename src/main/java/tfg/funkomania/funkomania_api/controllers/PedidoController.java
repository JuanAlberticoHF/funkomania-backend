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
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.CrearPedidoRequestDTO;
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.CrearPedidoResponseDTO;
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.PedidoCompletoDTOId;
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.VistaHistorialPedidosUsuarioDTOId;
import tfg.funkomania.funkomania_api.services.PedidoServiceImpl;

import java.util.List;

/**
 * <p>Controlador REST para manejar las solicitudes relacionadas con los pedidos de los usuarios.</p>
 * <p>Este controlador proporciona endpoints para crear pedidos a partir del carrito de compras, obtener el
 * historial de pedidos de un usuario y obtener los detalles de un pedido específico.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/")
@Validated
@Tag(name = "Gestor Pedidos", description = "Endpoints relacionados con la gestión de pedidos de los usuarios.")
@Slf4j
public class PedidoController {

    /** Servicio para gestionar las operaciones relacionadas con los pedidos. */
    private final PedidoServiceImpl pedidoService;

    public PedidoController(PedidoServiceImpl pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Crear pedido base al contenido del carrito", description = "Crea un nuevo pedido a partir del carrito de compras del usuario autenticado. El pedido se genera utilizando la información del carrito, incluyendo los productos seleccionados y sus cantidades.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido creado satisfactoriamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CrearPedidoResponseDTO.class)
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontró el usuario autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "409", description = "El carrito del usuario esta vacío, no se puede crear el pedido sin productos.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/pedidos/crear-desde-pedido")
    public ResponseEntity<CrearPedidoResponseDTO> crearPedido(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "DTO con los datos del identificador de la dirección y método de pago asociados a la compra y al nuevo pedido.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CrearPedidoRequestDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                        "idDireccion": 2,
                                        "idMetodoPago": 1,
                                        "comentarios": "Mi primer pedido"
                                    }
                                    """
                            )
                    ))
            @RequestBody CrearPedidoRequestDTO datosCrearPedido) {
        log.info("Creando pedido desde el carrito.");
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedidoDesdeCarrito(datosCrearPedido));
    }

    @Operation(summary = "Obtener historial de pedidos del usuario", description = "Obtiene todos los pedidos realizados por el usuario autenticado. Devuelve una lista de pedidos que representan el historial de compras del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Éxito, listado de pedidos del usuario obtenido satisfactoriamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaHistorialPedidosUsuarioDTOId.class)
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
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/usuario/pedidos")
    public ResponseEntity<List<VistaHistorialPedidosUsuarioDTOId>> obtenerPedidosUsuario() {
        log.info("Obteniendo historial de pedidos del usuario.");
        return ResponseEntity.ok(pedidoService.obtenerPedidosUsuario());
    }

    @Operation(summary = "Obtener detalles de un pedido específico", description = "Obtiene los detalles de un pedido específico realizado por el usuario autenticado, utilizando el ID del pedido para identificar cuál pedido se desea obtener. Devuelve la información completa del pedido, incluyendo los totales y las líneas de pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Éxito, pedido del usuario obtenido satisfactoriamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PedidoCompletoDTOId.class)
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontró el usuario o el producto no existe", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/usuario/pedidos/{idPedido}")
    public ResponseEntity<PedidoCompletoDTOId> obtenerPedidosUsuario(
            @Parameter(description = "ID del pedido a obtener")
            @Positive @PathVariable Long idPedido) {
        log.info("Obteniendo detalles del pedido con ID: {}.", idPedido);
        return ResponseEntity.ok(pedidoService.obtenerPedidoUsuarioPorId(idPedido));
    }
}

