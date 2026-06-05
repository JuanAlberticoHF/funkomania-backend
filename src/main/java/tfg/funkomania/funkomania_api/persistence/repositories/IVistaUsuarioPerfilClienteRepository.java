package tfg.funkomania.funkomania_api.persistence.repositories;

import org.springframework.data.repository.Repository;
import tfg.funkomania.funkomania_api.persistence.entities.VistaUsuarioPerfilCliente;

public interface IVistaUsuarioPerfilClienteRepository extends Repository<VistaUsuarioPerfilCliente, Long> {
    VistaUsuarioPerfilCliente findByEmail(String email);
}
