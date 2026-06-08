package tfg.funkomania.funkomania_api.services;

import org.springframework.stereotype.Service;
import tfg.funkomania.funkomania_api.dtos.metodoPago_dtos.MetodoPagoDTOId;
import tfg.funkomania.funkomania_api.persistence.repositories.IMetodoPagoRepository;

import java.util.List;

/**
 * <p>Servicio para obtener los métodos de pago Funkomania.</p>
 * <p>Esta clase implementa la interfaz {@link MetodoPagoService} y proporciona la lógica de negocio para la operación
 * relacionada con los métodos de pago activos.</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.5.0
 */
@Service
public class MetodoPagoServiceImpl implements MetodoPagoService {

    /** Repositorio para acceder a los datos de MetodoPago. */
    private final IMetodoPagoRepository metodoPagoRepository;

    public MetodoPagoServiceImpl(IMetodoPagoRepository metodoPagoRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
    }

    @Override
    public List<MetodoPagoDTOId> obtenerMetodosPagoActivos() {
        // Obtenemos los métodos de pago activos desde el repositorio, los convertimos a DTOs y los devolvemos como una lista.
        return metodoPagoRepository.findMetodoPagosByActivoIsTrue().stream()
                .map(MetodoPagoDTOId::new)
                .toList();
    }
}
