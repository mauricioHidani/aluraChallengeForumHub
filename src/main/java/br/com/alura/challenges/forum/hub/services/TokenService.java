package br.com.alura.challenges.forum.hub.services;

import br.com.alura.challenges.forum.hub.models.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${application.security.token.secret}")
    private String tokenSecret;

    @Value("${application.security.issuer-name}")
    private String issuerName;

    public String buildToken(final User user) {
        try {
            final var algorithm = Algorithm.HMAC256(tokenSecret);
            return JWT.create()
                    .withIssuer(issuerName)
                    .withSubject(user.getEmail())
                    .withExpiresAt(expireDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public String getSubject(final String tokenJWT) {
        try {
            final var algorithm = Algorithm.HMAC256(tokenSecret);
            return JWT.require(algorithm)
                    .withIssuer(issuerName)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token JWT é inválido ou expirado");
        }
    }

    private Instant expireDate() {
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
    }
}
