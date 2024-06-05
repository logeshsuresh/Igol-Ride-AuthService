package com.example.IgolAuthService.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.secret}")
    private String SECRET;

    /**
     * Creates a JWT token with the specified payload and email subject.
     * This method constructs a JWT token using the provided payload map, email subject,
     * and expiration time. It signs the token using the signing key generated from the
     * secret, ensuring its authenticity. The token is valid from the current time until
     * the expiration time specified by the `expiry` parameter.
     * @param payload a map containing additional data to include in the token
     * @param email the email address to be set as the subject of the token
     * @return the generated JWT token as a string
     */
    public String createToken(Map<String, Object> payload, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiry*1000L);
        return Jwts.builder()
                .claims(payload)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .subject(email)
                .signWith(getSignKey())
                .compact();
    }

    /**
     * Creates a JWT token with the specified email subject.
     * This method creates a JWT token with no additional payload data beyond the email address,
     * using an empty payload map. It sets the provided email address as the subject of the token.
     * @param email the email address to be set as the subject of the token
     * @return the generated JWT token as a string
     */
    public String createToken(String email) {
        return createToken(new HashMap<>(), email);
    }

    /**
     * Extracts all payloads (claims) from the given JWT token.
     *
     * This method parses the JWT token using the specified signing key
     * and retrieves all the claims contained within the token's body.
     * It returns the claims as a single Claims object.
     *
     * @param token the JWT token from which to extract payloads
     * @return the extracted payloads (claims) from the token
     */
    public Claims extractAllPayloads(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts a specific claim from the given JWT token using a claims resolver function.
     *
     * This method first retrieves all the claims from the JWT token using the extractAllPayloads method,
     * and then applies the provided claims resolver function to extract the desired claim.
     *
     * @param <T> the type of the claim to be extracted
     * @param token the JWT token from which to extract the claim
     * @param claimsResolver a function that extracts the specific claim from the claims
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllPayloads(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * This method uses the `extractClaim` method to extract the expiration date
     * from the JWT token's claims. It utilizes a method reference to the `getExpiration`
     * method of the Claims class as the claim resolver function.
     *
     * @param token the JWT token from which to extract the expiration date
     * @return the expiration date extracted from the token
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * This method checks if the token expiry was before the current time stamp or not ?
     * @param token JWT token
     * @return true if token is expired else false
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates the signing key for JWT token validation.
     *
     * This method creates a signing key for JWT token validation using HMAC SHA-256
     * based on a secret. It converts the secret string to a byte array using UTF-8 encoding
     * and then generates the HMAC SHA key.
     *
     * @return the signing key for JWT token validation
     */
    public Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Validates the JWT token against the provided email.
     *
     * This method extracts the email address from the JWT token and compares it with
     * the provided email. It also checks if the token has expired. The token is considered
     * valid if the email addresses match and the token is not expired.
     *
     * @param token the JWT token to validate
     * @param email the email address to compare with the one extracted from the token
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, String email) {
        final String userEmailFetchedFromToken = extractEmail(token);
        return (userEmailFetchedFromToken.equals(email)) && !isTokenExpired(token);
    }

    /**
     * Extracts a specific payload value from the JWT token.
     *
     * This method extracts all payloads (claims) from the JWT token and retrieves
     * the value associated with the specified payload key. It returns the value as an Object.
     *
     * @param token the JWT token from which to extract the payload
     * @param payloadKey the key of the payload to extract
     * @return the value associated with the specified payload key
     */
    public Object extractPayload(String token, String payloadKey) {
        Claims claim = extractAllPayloads(token);
        return (Object) claim.get(payloadKey);
    }

}