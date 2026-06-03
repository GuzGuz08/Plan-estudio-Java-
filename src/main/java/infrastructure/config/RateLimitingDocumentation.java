package infrastructure.config;

/**
 * RATE LIMITING — Documentación del Concepto
 * ============================================
 *
 * Rate limiting (limitación de tasa) controla la cantidad de peticiones
 * que un cliente puede hacer en un periodo de tiempo, previniendo:
 * - Ataques de fuerza bruta
 * - Denegación de servicio (DoS)
 * - Abuso de la API
 *
 * ==========================================
 * OPCIONES DE IMPLEMENTACIÓN:
 * ==========================================
 *
 * 1. BUCKET4J + Spring Boot Starter (recomendado para producción)
 *    - Dependencia: com.giffing.bucket4j.spring.boot.starter
 *    - Soporta almacenamiento distribuido (Redis, Hazelcast)
 *    - Configuración vía application.properties:
 *
 *      bucket4j.enabled=true
 *      bucket4j.filters[0].cache-name=rate-limit
 *      bucket4j.filters[0].url=/api/.*
 *      bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity=100
 *      bucket4j.filters[0].rate-limits[0].bandwidths[0].time=1
 *      bucket4j.filters[0].rate-limits[0].bandwidths[0].unit=minutes
 *
 * 2. RESILIENCE4J Rate Limiter
 *    - Dependencia: io.github.resilience4j:resilience4j-ratelimiter
 *    - Ideal para microservicios que ya usan Resilience4j
 *
 * 3. API GATEWAY (para arquitecturas de microservicios)
 *    - Spring Cloud Gateway con filtro RequestRateLimiter
 *    - Azure API Management
 *    - Kong, Nginx
 *
 * ==========================================
 * HEADERS DE RESPUESTA ESTÁNDAR:
 * ==========================================
 *    X-RateLimit-Limit: 100         (máximo de peticiones)
 *    X-RateLimit-Remaining: 95      (peticiones restantes)
 *    X-RateLimit-Reset: 1625000000  (timestamp de reinicio)
 *    Retry-After: 60                (segundos para reintentar, cuando se excede)
 *
 * ==========================================
 * ESTRATEGIA RECOMENDADA POR ENDPOINT:
 * ==========================================
 *    POST /api/personas  → 20 peticiones/minuto  (creación)
 *    PUT  /api/personas  → 30 peticiones/minuto  (actualización)
 *    GET  /api/personas  → 100 peticiones/minuto  (lectura)
 *    DELETE              → 10 peticiones/minuto  (eliminación)
 *
 * ==========================================
 * NOTA: No se implementa en esta fase porque requiere una dependencia
 * adicional y almacenamiento (Redis para entornos distribuidos).
 * Se recomienda implementar en la fase de preparación para producción.
 * ==========================================
 */
public final class RateLimitingDocumentation {
    private RateLimitingDocumentation() {
        // Clase de documentación - no instanciar
    }
}
