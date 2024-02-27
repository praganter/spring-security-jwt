package dev.batuhanyetgin.ms_security.utilty;


import dev.batuhanyetgin.ms_security.component.TokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String logStr = request.getProtocol()
                + " | METHOD : " + request.getMethod()
                + " | IP : " + request.getRemoteAddr()
                + " | PATH : " + request.getRequestURI()
                + " | QUERY : " + request.getQueryString();
        log.info(logStr);


        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String email = null;
        String token = null;

        if (authHeader != null && authHeader.contains("Bearer")) {
            token = authHeader.substring(7);
            try {
                email = tokenManager.getEmail(token);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if (tokenManager.validateToken(token)) {
                    UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
                    userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(userPassAuthToken);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        filterChain.doFilter(request, response);
    }

}
