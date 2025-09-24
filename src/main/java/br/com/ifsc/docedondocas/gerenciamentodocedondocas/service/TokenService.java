package br.com.ifsc.docedondocas.gerenciamentodocedondocas.service;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    final Dotenv config = Dotenv.configure().load();
    private String secret = config.get("TOKEN_SECRET");

    public String generateToken(Usuario usuario) {
        if ( secret.isEmpty() ){
            return null;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("gerenciamento-doce-dondocas")
                    .withSubject(usuario.getCpf())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro de criação de token", exception);
        }
    }

    public String validateToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("gerenciamento-doce-dondocas")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException exception) {
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
