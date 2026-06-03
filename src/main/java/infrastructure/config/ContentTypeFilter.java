package infrastructure.config;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(2)
public class ContentTypeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();

        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
            String contentType = httpRequest.getContentType();

            if (contentType == null || !contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
                httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                httpResponse.getWriter().write(
                        "{\"status\":415,\"error\":\"Unsupported Media Type\",\"mensaje\":\"Content-Type debe ser application/json\"}"
                );
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
