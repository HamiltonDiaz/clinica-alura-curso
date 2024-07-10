package med.voll.api.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface MedicoRepository extends JpaRepository <Medico, Long> {
    Page<Medico> findByActivoTrue(Pageable paginacion);

    @Query("""
        SELECT m FROM Medico m
        WHERE m.activo = 1 AND m.especialidad =:especialidad
        AND m.id not in (
            select c.medico.id from Consulta c
            c.data= :fecha
        )        
        ORDER BY RAND()
        LIMIT 1
       """)
    Medico seleccionarMedicoConEspecilidadEnFecha(Especialidad especialidad, LocalDate fecha);
}
