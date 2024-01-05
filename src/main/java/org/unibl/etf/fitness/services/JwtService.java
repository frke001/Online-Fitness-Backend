package org.unibl.etf.fitness.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.unibl.etf.fitness.models.dto.JwtUserDTO;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(JwtUserDTO userDetails);

    String generateToken(
            Map<String, Object> extraClaims,
            JwtUserDTO userDetails
    );

    boolean isTokenValid(String token, UserDetails userDetails);
}
