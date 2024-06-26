package med.voll.api.domain.usuarios;

import jakarta.validation.constraints.NotBlank;

public record DatosAutenticacionUsuario(
//        @NotBlank(message = "Nombre de usuario es obligatorio")
        String login,

//        @NotBlank(message = "Clave es obligatoria")
        String clave
) {
}
