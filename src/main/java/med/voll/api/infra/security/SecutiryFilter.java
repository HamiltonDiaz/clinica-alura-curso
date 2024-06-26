package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecutiryFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null){
            //Si el header viene con token bearer ingresa a esta lógica
            var token= tokenHeader.replace("Bearer ", "");//reemplaza la palabra bearer para tener solo el token
            var nombreUsuario = tokenService.getSubject(token);
            if (nombreUsuario != null){
                //Token valido y obligo un inicio de sesión en el sistema
                var usuario = usuarioRepository.findByLogin(nombreUsuario);
                var authentication = new UsernamePasswordAuthenticationToken(usuario,null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                //Esto es para que los parametros  .anyRequest() y .authenticated()
                //de archivo SecurityConfigurations.java método securityFilterChain se entere que el usuario
                //ya se autentico.
            }
        }

        //Si no viene con token y es la url del login entonces hace los filtros para avanzar
        filterChain.doFilter(request,response);


    }
}
