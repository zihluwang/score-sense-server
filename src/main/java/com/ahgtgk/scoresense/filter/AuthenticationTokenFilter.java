package com.ahgtgk.scoresense.filter;

import com.ahgtgk.scoresense.cache.UserCache;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.onixbyte.simplejwt.TokenResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final TokenResolver<DecodedJWT> tokenResolver;
    private final UserCache userCache;

    @Autowired
    public AuthenticationTokenFilter(TokenResolver<DecodedJWT> tokenResolver, UserCache userCache) {
        this.tokenResolver = tokenResolver;
        this.userCache = userCache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = request.getHeader("Authorization");
        if (Objects.isNull(token) || token.isBlank()) { // 没有令牌，提前结束
            filterChain.doFilter(request, response);
            return;
        }

        var claims = tokenResolver.resolve(token);
        var userId = Long.parseLong(claims.getSubject());
        var user = userCache.getUser(userId).toDomain();
        SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationToken
                .authenticated(user, null, user.getAuthorities()));
    }
}
