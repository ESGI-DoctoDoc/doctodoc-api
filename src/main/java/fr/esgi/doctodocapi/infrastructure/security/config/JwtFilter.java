package fr.esgi.doctodocapi.infrastructure.security.config;


import fr.esgi.doctodocapi.infrastructure.jwt.JwtService;
import fr.esgi.doctodocapi.use_cases.exceptions.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final List<String> EXCLUDE_URL = List.of(
            "/patients/login",
            "/doctors/login",
            "/users/validate-email",
            "/doctors/password-reset",
            "/patients/password-reset",
            "/doctors/register",
            "/patients/register",
            "/actuator/health"
    );

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractTokenFromRequest(request);

            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = this.jwtService.extractUserName(token);
                String role = this.jwtService.extractRole(token);
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role);

                User userDetails = new User(username, "", List.of(grantedAuthority));

                if (this.jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }


            filterChain.doFilter(request, response);

        } catch (Exception e) {
            throw new UnauthorizedException();
        }

    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        String token = null;

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        return token;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
}
