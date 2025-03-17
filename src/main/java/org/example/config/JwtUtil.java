package org.example.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtil {

    private final long tokenExpiration;

    private final Algorithm algorithm;

    private final JWTVerifier jwtVerifier;

    public JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.jwtVerifier = JWT.require(algorithm).build();
    }

    public String generateToken(String userName) {
        return JWT.create()
                .withSubject(userName)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + this.tokenExpiration))
                .sign(this.algorithm);
    }

    public String ExtractUserName(String token) {
        return verifyToken(token).getSubject();
    }

    private DecodedJWT verifyToken(String token) throws JWTVerificationException {
        return jwtVerifier.verify(token);
    }

    public boolean isTokenExpired(String token) {
        return verifyToken(token).getExpiresAt().before(new Date());
    }

    public boolean validateToken(String userName, String token) {
        return verifyToken(token).getSubject().equals(userName) && !isTokenExpired(token);
    }

}
