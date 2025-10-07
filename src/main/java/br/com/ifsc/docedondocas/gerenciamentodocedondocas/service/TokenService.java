package br.com.ifsc.docedondocas.gerenciamentodocedondocas.service;

import br.com.ifsc.docedondocas.gerenciamentodocedondocas.model.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
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
                    .withExpiresAt(genExpirationDate(2))
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

    public String generateTokenForgotPassword(Usuario usuario) {
        if ( secret.isEmpty() ){
            return null;
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("gerenciamento-doce-dondocas")
                    .withSubject(Long.toString(usuario.getId()))
                    .withClaim("type", "password_reset")
                    .withExpiresAt(genExpirationDate(1))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro de criação de token", exception);
        }
    }

    public Long validateTokenForgotPassword(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return Long.parseLong(JWT.require(algorithm)
                    .withIssuer("gerenciamento-doce-dondocas")
                    .withClaim("type", "password_reset")
                    .build()
                    .verify(token)
                    .getSubject());
        } catch (JWTCreationException exception) {
            return null;
        }
    }

    private Instant genExpirationDate(int horas){
        return LocalDateTime.now().plusHours(horas).toInstant(ZoneOffset.of("-03:00"));
    }
}
