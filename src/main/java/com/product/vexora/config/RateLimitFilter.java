package com.product.vexora.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Filtro de Rate Limiting para proteger endpoints de autenticação contra ataques de força bruta.
 * Limita requisições por IP para endpoints /auth/login e /auth/signup.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Cache de buckets por IP
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Configuração: 10 requisições por minuto por IP para endpoints de auth
    private static final int AUTH_REQUESTS_PER_MINUTE = 10;

    // Configuração: 100 requisições por minuto por IP para endpoints gerais
    private static final int GENERAL_REQUESTS_PER_MINUTE = 100;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String clientIp = getClientIp(request);

        // Aplicar rate limiting mais restritivo para endpoints de auth
        if (isAuthEndpoint(path)) {
            Bucket bucket = buckets.computeIfAbsent(clientIp + ":auth", this::createAuthBucket);

            if (!bucket.tryConsume(1)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write(
                    "{\"timestamp\":\"" + java.time.Instant.now() + "\"," +
                    "\"message\":\"Muitas tentativas. Aguarde um momento antes de tentar novamente.\"}"
                );
                return;
            }
        } else {
            // Rate limiting geral para outros endpoints
            Bucket bucket = buckets.computeIfAbsent(clientIp + ":general", this::createGeneralBucket);

            if (!bucket.tryConsume(1)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write(
                    "{\"timestamp\":\"" + java.time.Instant.now() + "\"," +
                    "\"message\":\"Limite de requisições excedido. Tente novamente em breve.\"}"
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthEndpoint(String path) {
        return path.startsWith("/auth/login") || path.startsWith("/auth/signup");
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private Bucket createAuthBucket(String key) {
        Bandwidth limit = Bandwidth.classic(
            AUTH_REQUESTS_PER_MINUTE,
            Refill.greedy(AUTH_REQUESTS_PER_MINUTE, Duration.ofMinutes(1))
        );
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket createGeneralBucket(String key) {
        Bandwidth limit = Bandwidth.classic(
            GENERAL_REQUESTS_PER_MINUTE,
            Refill.greedy(GENERAL_REQUESTS_PER_MINUTE, Duration.ofMinutes(1))
        );
        return Bucket.builder().addLimit(limit).build();
    }
}
