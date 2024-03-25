package com.hitachi.coe.fullstack.security.jwt;

import com.hitachi.coe.fullstack.constant.ErrorConstant;
import com.hitachi.coe.fullstack.security.services.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    /**
     * Filters incoming requests and checks if the user is authorized to perform the
     * requested action.
     *
     * @param request     the HTTP servlet request
     * @param response    the HTTP servlet response
     * @param filterChain the filter chain to be executed
     * @throws ServletException if the servlet encounters a problem
     * @throws IOException      if an input or output error occurs
     * @author Dat Tran
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")
                || authorizationHeader.trim().isEmpty()
                || authorizationHeader.substring(7).trim().isEmpty()) {
            SecurityContextHolder.clearContext();
            log.debug(ErrorConstant.MESSAGE_MISSING_INVALID_AUTHORIZATION_HEADER);
            filterChain.doFilter(request, response);
            return;
        }

        final String uri = request.getRequestURI().toLowerCase();
        final int apiWordIdx = uri.indexOf("api");
        if (apiWordIdx == -1) {
            final String message = ErrorConstant.MESSAGE_URI_MALFORMED_NOT_CONTAINS_API + " (uri: " + uri + " )";
            log.debug(message);
            throw new AccessDeniedException(message);
        }
        final String module = "/" + uri.substring(apiWordIdx);
        final String fullModule = (request.getMethod() + "_" + module).toUpperCase();
        final Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();
        final boolean isAuthorized = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> matchAuthority(auth, fullModule)
                        && matchBothWithSlashOrWithoutSlashOrAllWildCard(auth, fullModule));
        if (!isAuthorized) {
            log.debug(ErrorConstant.MESSAGE_PERMISSION_ACCESS_DENIED);
            throw new AccessDeniedException(ErrorConstant.MESSAGE_PERMISSION_ACCESS_DENIED);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 
     * @param authority        the authority string that come from logged in user's
     *                         authorities
     * @param requestAuthority the request authority string to be matched
     * @return a boolean, if true then the request is authorized, otherwise false,
     *         using AntPathMatcher
     * @author tminhto
     * @see UserDetailsImpl
     * @see AntPathMatcher
     */
    private boolean matchAuthority(final String authority, final String requestAuthority) {
        // replace the first occurrence of '{' and everything after it with '**'
        String authorityPattern = authority.replaceFirst("\\{[^{}]*}", "**");
        if (authorityPattern.endsWith("/")) {
            authorityPattern += "**";
        }
        final AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match(authorityPattern, requestAuthority);
    }

    /**
     * Checks if the authority and the request authority match both with slash or
     * without slash or all wild card.
     * 
     * @param authority        the authority string to be matched, come from logged
     *                         in user's authorities
     * @param requestAuthority the request authority to be matched
     * @return true if the authority and the request authority match both with slash
     *         or without slash or all wild card, false otherwise
     * @author tminhto
     */
    private boolean matchBothWithSlashOrWithoutSlashOrAllWildCard(final String authority,
            final String requestAuthority) {
        // Remove HTTP method prefix from both authorities
        final String authorityWithoutMethod = removeHttpMethodFromStartOfString(authority, "_");
        final String requestAuthorityWithoutMethod = removeHttpMethodFromStartOfString(requestAuthority, "_");
        // check if both authorities and request authority start with "/api" or "api"
        final boolean bothStartWithSlash = authorityWithoutMethod.startsWith("/api")
                && requestAuthorityWithoutMethod.startsWith("/api");
        final boolean bothStartWithoutSlash = authorityWithoutMethod.startsWith("api")
                && requestAuthorityWithoutMethod.startsWith("api");
        boolean isAllWildCard = false;
        if (!bothStartWithSlash && !bothStartWithoutSlash) {
            isAllWildCard = authorityWithoutMethod.startsWith("/**");
        }
        return (bothStartWithSlash || bothStartWithoutSlash || isAllWildCard);
    }

    /**
     * Removes the HTTP method from the start of a string.
     * 
     * @param str              the string to be processed
     * @param beginWordToSlice the word to slice from the start of the string,
     *                         ex: GET_/some/mock/url/path, if beginWordToSlice is
     *                         "_" then return /some/mock/url/path
     * @return the string without the HTTP method at the start
     */
    private String removeHttpMethodFromStartOfString(final String str, final String beginWordToSlice) {
        final String lowerStr = str.toLowerCase();
        return lowerStr.substring(lowerStr.indexOf(beginWordToSlice) + 1);
    }
}
