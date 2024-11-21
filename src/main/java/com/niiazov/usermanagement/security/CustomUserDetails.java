package com.niiazov.usermanagement.security;

import com.niiazov.usermanagement.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public record CustomUserDetails(User user) implements UserDetails {

    // Преобразование ролей в коллекцию GrantedAuthority's
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Всегда возвращает, что аккаунт не истек
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Всегда возвращает, что аккаунт не заблокирован
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Всегда возвращает, что учетные данные не истекли
    }

    @Override
    public boolean isEnabled() {
        return true; // Всегда возвращает, что аккаунт активен
    }

}
