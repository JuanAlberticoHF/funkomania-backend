package tfg.funkomania.funkomania_api.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * <p>Entidad que representa un categoria en el sistema de Funkomania.</p>
 * <p>La entidad mapea tabla {@code categoria} de la base de datos</p>
 *
 * @author JuanAlbeticoHF
 * @version 1.0.0
 * @since 0.2.0
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "Categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCategoria", nullable = false)
    private Long id;

    @NotBlank(message = "El nombre de la categoría no debe estar vacío.")
    @Size(max = 50, message = "El nombre de la categoría no debe exceder los 50 caracteres.")
    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoriaPadre")
    @EqualsAndHashCode.Exclude
    private Categoria categoriaPadre;
}
