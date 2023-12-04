package com.azry.lms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> tokenOptional = getTokenFromRequest(request);
        if (tokenOptional.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        validateTokenAndUpdateSecurityContext(request, tokenOptional.get());
        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER_NAME);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
            return Optional.of(bearerToken.substring(AUTH_HEADER_VALUE_PREFIX.length()));
        }
        return Optional.empty();
    }

    private void validateTokenAndUpdateSecurityContext(HttpServletRequest request, String token) {
        String username = jwtTokenProvider.getUsername(token);
        SecurityContext context = SecurityContextHolder.getContext();

        if (Objects.nonNull(username) && Objects.isNull(context.getAuthentication())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtTokenProvider.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
            }
        }
    }
}
