package com.darkness.apiGatewayService.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter implements Filter {

    @Value("${jwt.secret}")
    private String secretKey;

    // Exclude login/register endpoints
    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/auth/login",
            "/api/auth/register"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        String path = httpReq.getRequestURI();

        // Skip JWT check for excluded URLs
        if (EXCLUDE_URLS.stream().anyMatch(path::startsWith)) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpReq.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpRes.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7); // remove "Bearer "

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Optional: pass user info to downstream services
            httpReq.setAttribute("userId", claims.getSubject());
            httpReq.setAttribute("role", claims.get("role"));
        } catch (Exception e) {
            httpRes.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpRes.getWriter().write("Invalid or expired token");
            return;
        }

        chain.doFilter(request, response);
    }
}
