package tfg.funkomania.funkomania_api.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tfg.funkomania.funkomania_api.dtos.producto_dtos.ProductoDTOId;
import tfg.funkomania.funkomania_api.services.ListaDeseosServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/usuario/lista-deseos")
@Tag(name = "Gestion Lista de Deseos", description = "Endpoints para gestionar la lista de deseos del cliente autenticado, incluyendo la obtención de su lista de deseos, la adición de productos a la lista y la eliminación de productos de la lista.")
public class ListaDeseosController {
    /** Servicio de lista de deseos que contiene la lógica de negocio de todas las operaciones */
    private final ListaDeseosServiceImpl listaDeseosService;

    private ListaDeseosController(ListaDeseosServiceImpl listaDeseosService) {
        this.listaDeseosService = listaDeseosService;
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/")
    public ResponseEntity<List<ProductoDTOId>> obtenerListaDeseosDelUsuario() {
        return ResponseEntity.ok(listaDeseosService.obtenerListaDeseosDelUsuario());
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{idProducto}")
    public ResponseEntity<Void> addProductoAListaDeseos(@PathVariable Long idProducto) {
        listaDeseosService.agregarProductoListaDeseosDelUsuario(idProducto);
        return ResponseEntity.ok().build();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> eliminarProductoDeListaDeseos(@PathVariable Long idProducto) {
        listaDeseosService.eliminarProductoListaDeseosDelUsuario(idProducto);
        return ResponseEntity.ok().build();
    }
}
