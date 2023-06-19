package com.minsait.api.security.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.minsait.api.security.details.CustomUserDetails;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class UserDetailUtil {

    public static UserDetails getUserDetail(String userName, List<String> authorities) {
        return User.withUsername(userName)
                .roles(authorities.stream()
                        .filter(a -> a.startsWith("ROLE_"))
                        .map(a -> a.replace("ROLE_", ""))
                        .toArray(String[]::new))
                .authorities(
                        authorities.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()))
                .password("")
                .build();
    }

    public static CustomUserDetails getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isCustomDetail = auth.getPrincipal() instanceof CustomUserDetails;
        if (!isCustomDetail) {
            throw new AccessDeniedException("Usuário não autenticado ou token expirado");
        }

        return (CustomUserDetails) auth.getPrincipal();
    }

    public static CustomUserDetails getAuthenticatedUserOrELseNull() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(auth)) {
            return null;
        }
        boolean isCustomDetail = auth.getPrincipal() instanceof CustomUserDetails;
        if (!isCustomDetail) {
            return null;
        }

        return (CustomUserDetails) auth.getPrincipal();
    }
}
