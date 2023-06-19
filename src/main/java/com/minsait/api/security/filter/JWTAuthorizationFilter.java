package com.minsait.api.security.filter;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.minsait.api.security.details.CustomUserDetails;
import com.minsait.api.security.util.JWTUtil;
import com.minsait.api.security.util.UserDetailUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTUtil jwtUtil;

    private UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
            UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader("Authorization");
        if (Objects.nonNull(header) && header.startsWith("Bearer ")) {
            UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
            if (Objects.nonNull(auth)) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (jwtUtil.isValidToken(token)) {
            Claims claims = jwtUtil.getClaims(token);
            String username = (String) claims.get("user_name");
            Integer userId = (Integer) claims.get("user_id");

            ArrayList<String> authorities = (ArrayList<String>) claims.get("authorities");

            UserDetails user = UserDetailUtil.getUserDetail(username, authorities);

            CustomUserDetails userCustom = new CustomUserDetails(user.getUsername(), user.getPassword(),
                    user.getAuthorities());
            if (Objects.nonNull(userId)) {
                userCustom.setUserId(userId.longValue());
            }
            return new UsernamePasswordAuthenticationToken(userCustom, null, userCustom.getAuthorities());
        }
        return null;
    }
}
