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
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.pedido_dtos.*;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPagoEnum;
import tfg.funkomania.funkomania_api.persistence.enums.EstadoPedidoEnum;
import tfg.funkomania.funkomania_api.services.PedidoServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>Controlador REST para manejar las operaciones relacionadas con los pedidos desde la perspectiva del administrador.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/admin/pedidos")
@Validated
@Tag(name = "[ADMIN] Gestor Pedidos", description = "Operaciones relacionadas con los pedidos para el administrador")
public class PedidoAdminController {

    /** Servicio para manejar la lógica de negocio relacionada con los pedidos. */
    private final PedidoServiceImpl pedidoService;

    public PedidoAdminController(PedidoServiceImpl pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Obtener listado de pedidos", description = "Obtiene todos los pedidos que coinciden con los criterios de búsqueda.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de pedidos obtenido satisfactoriamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaPedidosAdminDTOId.class)
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: el usuario no tiene permiso para acceder al recurso", content = @Content(
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
    public ResponseEntity<List<VistaPedidosAdminDTOId>> getAllPedidosAdmin(
            @Parameter(description = "ID del pedido a buscar") @RequestParam(required = false) Long idPedido,
            @Parameter(description = "Código del pedido a buscar") @RequestParam(required = false) String codigoPedido,
            @Parameter(description = "Nombre o email del usuario") @RequestParam(required = false) String usuario,
            @Parameter(description = "Fecha del pedido") @RequestParam(required = false) LocalDateTime fechaPedido,
            @Parameter(description = "Estado del pedido") @RequestParam(required = false) EstadoPedidoEnum estadoPedido,
            @Parameter(description = "Estado del pago") @RequestParam(required = false) EstadoPagoEnum estadoPago,
            @Parameter(description = "Método de pago") @RequestParam(required = false) String metodoPago
    ) {
        return ResponseEntity.ok().body(pedidoService.getAllPedidosAdmin(idPedido, codigoPedido, usuario, fechaPedido, estadoPedido, estadoPago, metodoPago));
    }

    @Operation(summary = "Obtener detalles de un pedido", description = "Obtiene los detalles completos de un pedido específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido obtenido satisfactoriamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PedidoCompletoDTOId.class)
            )),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: el usuario no tiene permiso para acceder al recurso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{idPedido}")
    public ResponseEntity<PedidoCompletoDTOId> obtenerPedidoUsuarioPorId(
            @Parameter(description = "ID del pedido", required = true) @PathVariable Long idPedido) {
        return ResponseEntity.ok().body(pedidoService.obtenerPedidoUsuarioPorId(idPedido));
    }

    @Operation(summary = "Crear nuevo pedido", description = "Crea un nuevo pedido manualmente por el administrador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado satisfactoriamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(
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
            @ApiResponse(responseCode = "404", description = "Usuario, dirección o método de pago no encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/")
    public ResponseEntity<Void> crearPedidoParaUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para crear el pedido",
                    required = true,
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CrearPedidoAdminRequestDTO.class),
                        examples = @ExampleObject(
                            name = "Ejemplo de creación de pedido",
                            value = """
                            {
                                "idUsuario": 2,
                                "idDireccion": 2,
                                "idMetodoPago": 2,
                                "estadoPedido": "PENDIENTE",
                                "estadoPago": "PENDIENTE",
                                "comentarios": "Esto es un comentario",
                                "productos": [
                                    {
                                        "idProducto": 1,
                                        "precioUnitarioSinIVA": 20,
                                        "iva": 21,
                                        "cantidad": 5
                                    }
                                ]
                            }
                            """
                        )
                    ))
            @RequestBody @Validated CrearPedidoAdminRequestDTO datosCrearPedido) {
        pedidoService.crearPedidoParaUsuario(datosCrearPedido);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Actualizar datos generales del pedido", description = "Actualiza el estado, pago o comentarios de un pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado satisfactoriamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PedidoCompletoDTOId.class)
            )),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(
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
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{idPedido}")
    public ResponseEntity<PedidoCompletoDTOId> actualizarPedido(
            @Parameter(description = "ID del pedido", required = true) @PathVariable Long idPedido,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para actualizar el pedido", required = true)
            @RequestBody @Validated AdminUpdatePedidoRequestDTO datosActualizarPedido) {
        return ResponseEntity.ok().body(pedidoService.actualizarDatosPedido(idPedido, datosActualizarPedido));
    }

    @Operation(summary = "Agregar producto al pedido", description = "Agrega una nueva línea de producto a un pedido existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto agregado satisfactoriamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PedidoCompletoDTOId.class)
            )),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(
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
            @ApiResponse(responseCode = "404", description = "Pedido o producto no encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{idPedido}/lineas")
    public ResponseEntity<PedidoCompletoDTOId> agregarUnNuevoProductoAlPedido(
            @Parameter(description = "ID del pedido", required = true) @PathVariable Long idPedido,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la línea a agregar", required = true)
            @RequestBody @Validated AdminAgregarLineaPedidoRequestDTO datosAgregarLineaPedido) {
        return ResponseEntity.ok().body(pedidoService.agregarUnNuevoProductoAlPedido(idPedido, datosAgregarLineaPedido));
    }

    @Operation(summary = "Actualizar línea de pedido", description = "Actualiza los datos de una línea de producto en un pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Línea actualizada satisfactoriamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PedidoCompletoDTOId.class)
            )),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(
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
            @ApiResponse(responseCode = "404", description = "Pedido o línea de pedido no encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{idPedido}/lineas/{idProducto}")
    public ResponseEntity<PedidoCompletoDTOId> actualizarDatosDetallePedido(
            @Parameter(description = "ID del pedido", required = true) @PathVariable Long idPedido,
            @Parameter(description = "ID del producto", required = true) @PathVariable Long idProducto,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para actualizar la línea", required = true)
            @RequestBody @Validated AdminUpdateProductoPedidoRequestDTO datosActualizarLineaPedido
    ) {
        return ResponseEntity.ok().body(pedidoService.actualizarDatosDetallePedido(idPedido, idProducto, datosActualizarLineaPedido));
    }

    @Operation(summary = "Eliminar línea de pedido", description = "Elimina una línea de producto de un pedido existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Línea eliminada satisfactoriamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: el usuario no tiene permiso para acceder al recurso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "Pedido o línea de pedido no encontrado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{idPedido}/lineas/{idProducto}")
    public ResponseEntity<Void> eliminarDetallePedido(
            @Parameter(description = "ID del pedido", required = true) @PathVariable Long idPedido,
            @Parameter(description = "ID del producto", required = true) @PathVariable Long idProducto) {
        pedidoService.eliminarDetallePedido(idPedido, idProducto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
