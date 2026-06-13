package tfg.funkomania.funkomania_api.controllers.admin_controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tfg.funkomania.funkomania_api.persistence.entities.VistaAdminAlertasStockDTOId;
import tfg.funkomania.funkomania_api.services.AlertasStockServiceImpl;

import java.util.List;

/**
 * <p>Controlador REST para manejar las solicitudes relacionadas con las categorías de productos para administradores</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/admin/alertas-stock")
@Tag(name = "[ADMIN] Alertas de Stock", description = "Operaciones relacionadas con las alertas de stock para el administrador")
@Slf4j
public class AlertasStockAdminController {

    /** Servicio para manejar la lógica de negocio relacionada con las alertas de stock. */
    private final AlertasStockServiceImpl alertasStockService;

    public AlertasStockAdminController(AlertasStockServiceImpl alertasStockService) {
        this.alertasStockService = alertasStockService;
    }

    @Operation(summary = "Obtener alertas de stock", description = "Obtiene una lista de alertas de stock para el administrador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alertas de stock obtenida correctamente", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = VistaAdminAlertasStockDTOId.class)
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
    public ResponseEntity<List<VistaAdminAlertasStockDTOId>> getAlertasStock() {
        log.info("Obteniendo alertas de stock para administrador.");
        return ResponseEntity.status(HttpStatus.OK).body(alertasStockService.obtenerAlertasStock());
    }
}
