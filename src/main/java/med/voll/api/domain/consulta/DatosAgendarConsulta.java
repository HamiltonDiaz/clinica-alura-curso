package med.voll.api.domain.consulta;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.medico.Especialidad;

import java.time.LocalDate;

public record DatosAgendarConsulta(
        Long id,
        @NotNull
        Long idPaciente,

        Long idMedico,
        @NotNull
        @Future
        //@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDate fecha,
        Especialidad especialidad
        ) {
}
