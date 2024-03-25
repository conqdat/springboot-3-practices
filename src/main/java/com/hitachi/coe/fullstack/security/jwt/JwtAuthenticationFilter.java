package com.hitachi.coe.fullstack.security.jwt;

import com.hitachi.coe.fullstack.util.JwtUtils;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A filter class that handles JWT authentication for requests
 * It check the Authorization header for a valid JWT token and sets
 * the user authentication in the SecurityContext
 *
 * @author tminhto
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Overrides the doFilterInternal method of the OncePerRequestFilter class to implement JWT-based authentication.
     * Extracts the JWT token from the request header and validates its signature and expiration date using the JwtUtils class.
     * If the token is valid, retrieves the username from the token's subject claim, loads the UserDetails object from the database using the UserDetailsService,
     * creates a new UsernamePasswordAuthenticationToken, sets the authentication details from the request, and sets the token in the request attributes.
     * If the token is invalid, clears the security context and throws an JwtException.
     * If the Authorization header is missing or invalid, clears the security context and logs a debug message.
     *
     * @param request the HttpServletRequest object representing the incoming request
     * @param response the HttpServletResponse object representing the outgoing response
     * @param filterChain the FilterChain object representing the chain of filters to be applied to the request
     * @throws ServletException if an error occurs while processing the request
     * @throws IOException if an error occurs while writing to the response
     * @throws JwtException if the JWT token is invalid
     * @see UserDetailsService
     * @see UserDetails
     * @see UsernamePasswordAuthenticationToken
     * @see WebAuthenticationDetailsSource
     * @author tminhto
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // if header "Authorization" is not present or does not start with "Bearer "
        // or is empty, clear the context and throw error
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")
                || authorizationHeader.trim().isEmpty()) {
            SecurityContextHolder.clearContext();
            log.debug("Missing or invalid Authorization header");
            filterChain.doFilter(request, response);
            return;
        }
        try {
            final String jwtAccessToken = authorizationHeader.substring(7);
            final String username = jwtUtils.extractUsername(jwtAccessToken);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (username != null && authentication == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtils.validateJwtToken(jwtAccessToken)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    request.setAttribute("accessToken", jwtAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.debug("Invalid JWT token (expired, malformed, etc...), can not get username from token");
            throw new JwtException("Invalid JWT token (expired, malformed, etc...), can not get username from token", e);
        }
        filterChain.doFilter(request, response);
    }
}
