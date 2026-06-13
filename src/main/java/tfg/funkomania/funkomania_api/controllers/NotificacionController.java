package tfg.funkomania.funkomania_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.notificacion_dtos.VistaNotificacionUsuarioDTOId;
import tfg.funkomania.funkomania_api.dtos.usuario_dtos.VistaUsuarioPerfilClienteDTOId;
import tfg.funkomania.funkomania_api.services.NotificacionServiceImpl;

import java.util.List;

/**
 * <p>Controlador REST para manejar las solicitudes relacionadas con las notificaciones del cliente</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.1
 * @since 0.5.0
 */
@RestController
@RequestMapping("/usuario/notificaciones")
@Tag(name = "Gestor de Notificaciones", description = "Endpoints para gestionar las notificaciones de los usuarios, incluyendo la visualización y actualización del estado de las notificaciones.")
@Slf4j
public class NotificacionController {

    /** Servicio de notificaciones */
    private final NotificacionServiceImpl notificacionService;

    public NotificacionController(NotificacionServiceImpl notificacionService) {
        this.notificacionService = notificacionService;
    }

    @Operation(summary = "Obtener las notificaciones del cliente", description = "Obtiene la lista de notificaciones asociadas al cliente autenticado. Requiere que el usuario esté autenticado para acceder a esta información.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificaciones del cliente autenticado obtenidas exitosamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaUsuarioPerfilClienteDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    [
                                       {
                                         "idNotificacion": 1,
                                         "idUsuario": 1,
                                         "tipoNotificacion": "REGISTRO",
                                         "estadoNotificacion": "PENDIENTE",
                                         "mensaje": "Mensaje de notificación de ejemplo"
                                       }
                                     ]
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
    public ResponseEntity<List<VistaNotificacionUsuarioDTOId>> obtenerTodasLasNotificacionesDelCliente () {
        log.info("Obteniendo todas las notificaciones del usuario.");
        return ResponseEntity.ok(notificacionService.obtenerTodasLasNotificacionesDelUsuario());
    }

    @Operation(summary = "Leer notificación del cliente", description = "Marca una notificación específica como leída para el cliente autenticado. Requiere que el usuario esté autenticado para acceder a esta información.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificaciones del cliente autenticado leída exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado - el usuario no está autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "404", description = "No se encontró al usuario autenticado o la notificación", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "409", description = "Conflicto: La notificación ya ha sido marcada como leída o no pertenece al usuario autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener el perfil del cliente autenticado", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProblemDetail.class)
            )),
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{idNotificacion}/leer")
    public ResponseEntity<Void> leerNotificacion (@PathVariable Long idNotificacion) {
        log.info("Marcando notificación con ID: {} como leída.", idNotificacion);
        notificacionService.leerNotificacion(idNotificacion);
        return ResponseEntity.ok().build();
    }
}
