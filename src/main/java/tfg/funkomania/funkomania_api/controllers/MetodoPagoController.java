package tfg.funkomania.funkomania_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tfg.funkomania.funkomania_api.dtos.metodoPago_dtos.MetodoPagoDTOId;
import tfg.funkomania.funkomania_api.services.MetodoPagoServiceImpl;

import java.util.List;

/**
 * <p>Controlador REST para los metodos de pago del sistema</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
@RestController
@RequestMapping("/metodos-pago")
@Tag(name = "Gestor de Métodos de Pago", description = "Endpoint para obtener los metodos de pago activos en el sistema")
public class MetodoPagoController {

    /** Servicio de métodos de pago */
    private final MetodoPagoServiceImpl metodoPagoService;

    public MetodoPagoController(MetodoPagoServiceImpl metodoPagoService) {
        this.metodoPagoService = metodoPagoService;
    }

    @Operation(summary = "Obtener todos los metodos de pago activos", description = "Retorna una lista de todos los metodos de pago activos en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de objetos JSON con los datos de cada metodo de pago activo", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MetodoPagoDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    [
                                      {
                                        "id": 1,
                                        "nombre": "Metodo de Pago 1",
                                        "activo": true
                                      },
                                      {
                                        "id": 2,
                                        "nombre": "Metodo de Pago 2",
                                        "activo": true
                                      }
                                    ]
                                    """
                    )
            )),
    })
    @GetMapping("/")
    public ResponseEntity<List<MetodoPagoDTOId>> obtenerMetodosPagoActivos() {
        return ResponseEntity.ok(metodoPagoService.obtenerMetodosPagoActivos());
    }
}
