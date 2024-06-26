package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter //Lombok para crear getters
@NoArgsConstructor //Lombok para crear un constructor sin atributos
@AllArgsConstructor //Lombok para crear un constructor con todos los atributos
@EqualsAndHashCode (of="id") // para que compara los medicos con el id
public class Medico {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String telefono;

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    private String documento;

    private Boolean activo;

    @Embedded
    private Direccion direccion;

    public Medico(DatosRegistroMedico datosRegistroMedico) {
        this.nombre= datosRegistroMedico.nombre();
        this.email= datosRegistroMedico.email();
        this.telefono= datosRegistroMedico.telefono();
        this.especialidad= datosRegistroMedico.especialidad();
        this.documento= datosRegistroMedico.documento();
        this.activo= true;
        this.direccion = new Direccion(datosRegistroMedico.direccion());
    }

    public void actualizarDatos(DatosActualizarMedico datosActualizarMedico) {
        if (datosActualizarMedico.nombre()!= null){
            this.nombre= datosActualizarMedico.nombre();
        }
        if (datosActualizarMedico.documento()!= null){
            this.documento= datosActualizarMedico.documento();
        }
        if (datosActualizarMedico.direccion()!= null){
            this.direccion = direccion.actualizarDatos(datosActualizarMedico.direccion());
        }
    }

    public void desactivarMedico() {
        this.activo= false;
    }
}
