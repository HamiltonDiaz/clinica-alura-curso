package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico,
                                          UriComponentsBuilder uriComponentsBuilder){
        //@Valid -> es para aplicar las validaciones de los modelo antes de registrar en la bd
        Medico medico= medicoRepository.save(new Medico(datosRegistroMedico));
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico);

        URI url= uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaMedico);
    }

/*    @GetMapping
    public List<DatosListadoMedico> listadoMedicos(){
            return medicoRepository.findAll().stream().map(DatosListadoMedico::new).toList();
    }*/

    @GetMapping
    public ResponseEntity<Page <DatosListadoMedico>> listadoMedicos(@PageableDefault(size = 2) Pageable paginacion){
        //@PageableDefault(size = 2) -> esto significa 2 registros por página
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);

        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new));

        //http://localhost:8080/medicos?size=20 Esto quiere decir que 20 registros por páginas

        //http://localhost:8080/medicos?size=2&page=3
        //Esto quiere decir que 2 registros por páginas y muestre los registros de la pagina 3

        //http://localhost:8080/medicos?size=2&page=3&sort=nombre
        //Esto quiere decir que 2 registros por páginas, muestre
        //los registros de la pagina 3 y ordenado por el campo nombre
        //¡NOTA! el atributo de orden (sort) debe ser el mismo que el que esta definido en el modelo
    }

    @PutMapping
    @Transactional //Esto es para que hibernate valide que la transacción se libera
    public ResponseEntity<DatosRespuestaMedico>  actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico){
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());
        medico.actualizarDatos(datosActualizarMedico);
        return ResponseEntity.ok(new DatosRespuestaMedico(medico));
    }

    //Delete en base de datos
    /*@DeleteMapping("/{id}")
    @Transactional
    public void eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medicoRepository.delete(medico);
    }   */

    //Delete lógica... cambiar estado
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        medico.desactivarMedico();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity <DatosRespuestaMedico>retornaDatosMedico(@PathVariable Long id){
        Medico medico = medicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosRespuestaMedico(medico));
    }
}
