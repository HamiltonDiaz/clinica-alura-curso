package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import med.voll.api.domain.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {
    @Value("${api.security.secret}")//viene del archivo application.properties
    private String apiSecret;

    public String generarToken(Usuario usuario){
        try {
//            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("voll med")//nombre de la aplicación
                    .withSubject(usuario.getLogin())//Token dirigido
                    .withClaim("id",usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())//Expiración del token
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }
    }

    public String getSubject(String token){
        //Metodo propio parar generar el token de JWT
        if (token == null) {
            throw new RuntimeException();
        }
        DecodedJWT verifier=null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret); // validando firma
            verifier = JWT.require(algorithm)
                    .withIssuer("voll med")
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception){
            System.out.println(exception.toString());
        }

        if (verifier.getSubject() == null) {
            throw new RuntimeException("Verifier invalido");
        }
        return verifier.getSubject();
    }


    private Instant generarFechaExpiracion(){
        //Hora GTM -5 Bogotá Lima...
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }
}
