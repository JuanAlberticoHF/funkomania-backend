package tfg.funkomania.funkomania_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tfg.funkomania.funkomania_api.dtos.categoria_dtos.CategoriaDTOId;
import tfg.funkomania.funkomania_api.services.CategoriaServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Controlador REST para manejar las solicitudes relacionadas con las categorías de productos.</p>
 * <p>Proporciona un endpoint para obtener todas las categorías disponibles.</p>
 *
 * @author JuanAlbeticoHF
 * @version 0.1.1
 * @since 0.2.0
 */
@RestController
@RequestMapping("/categorias")
@Tag(name = "Gestor de Categorías", description = "Endpoints para gestionar las categorías de productos disponibles en el sistema.")
@Slf4j
public class CategoriaController {

    /** Servicio categorías. */
    private final CategoriaServiceImpl categoriaService;

    public CategoriaController(CategoriaServiceImpl categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Operation(summary = "Obtener todos las categorías", description = "Retorna una lista de todas las categorías disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de objetos JSON con los datos de cada categoria y su categoria padre", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CategoriaDTOId.class),
                    examples = @ExampleObject(
                            value = """
                                    [
                                      {
                                        "id": 1,
                                        "nombre": "Padre",
                                        "categoriaPadre": null
                                      },
                                      {
                                        "id": 2,
                                        "nombre": "Hija",
                                        "categoriaPadre": {
                                          "id": 1,
                                          "nombre": "Padre",
                                          "categoriaPadre": null
                                        }
                                      }
                                    ]
                                    """
                    )
            ))
    })
    @GetMapping("/")
    public ResponseEntity<List<CategoriaDTOId>> getAllCategorias() {
        log.info("Obteniendo todas las categorías.");
        List<CategoriaDTOId> categorias = new ArrayList<>();
        categoriaService.getAllCategorias().forEach(categoria -> categorias.add(new CategoriaDTOId(categoria)));
        return ResponseEntity.status(HttpStatus.OK).body(categorias);
    }
}
