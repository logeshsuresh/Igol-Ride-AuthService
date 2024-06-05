package com.example.IgolAuthService.helpers;

import com.example.IgolAuthService.models.Passenger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// Spring Security works on UserDetails polymorphic type for auth

public class AuthPassengerDetails extends Passenger implements UserDetails {

    private final String username;

    private final String password;

    public AuthPassengerDetails(Passenger passenger) {
        this.username = passenger.getEmail();
        this.password = passenger.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // Below set of methods are not much of a concern

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
