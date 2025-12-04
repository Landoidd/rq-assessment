package com.challenge.api.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/*
Mock class for JWT verification.
 */
public class JwtUtil {
    /**
     * Extracts JWT token from Authorization header.
     * 
     * @param request
     * @return
     */
    private static final String ALGORITHM = "HS256";
    private static final String SECRET = "secret";

    public static String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring("Bearer ".length());
        if (token == null || token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }
        return token;
    }

    /**
     * Verifies a JWT token.
     * @param token
     * @return
     */
    public static boolean verifyToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            parseJwt(token, ALGORITHM, SECRET);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Parses JWT token and compares algorithm and secret.
     * @param token
     * @param algorithm
     * @param secret
     * @return
     */
    public static boolean parseJwt(String token, String algorithm, String secret) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        return ALGORITHM.equals(algorithm) && SECRET.equals(secret);
    }

    /**
     * Verifies JWT token from request and throws exception if invalid.
     * @param request
     */
    public static void verifyTokenFromRequest(HttpServletRequest request) {
        String token = extractToken(request);
        if (!verifyToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired JWT token");
        }
    }
}
