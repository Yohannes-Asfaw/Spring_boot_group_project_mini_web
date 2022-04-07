package com.kelem.kelem.model;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * A custom user detail. Totally aware of the awful naming. To be fixed.
 */
public class MyUserDetail implements UserDetails {

    private UserModel user;
    public MyUserDetail(UserModel user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + this.user.role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // just for now we return True
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // just for now we return True
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // just for now we return True
        return true;
    }

    @Override
    public boolean isEnabled() {
        // just for now we return True
        return true;
    }
    
}
