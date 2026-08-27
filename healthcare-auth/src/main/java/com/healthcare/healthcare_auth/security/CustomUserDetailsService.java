package com.healthcare.healthcare_auth.security;

import com.healthcare.healthcare_auth.entity.Permission;
import com.healthcare.healthcare_auth.entity.Role;
import com.healthcare.healthcare_auth.entity.Users;
import com.healthcare.healthcare_auth.repository.UsersRepository;
import com.healthcare.healthcare_auth.service.LoginAttemptService;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public CustomUserDetailsService(UsersRepository userRepository, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        Users user = userRepository.findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        loginAttemptService.unlockIfExpired(user);
        Collection<GrantedAuthority> authorities = buildAuthorities(user);
        return new AuthenticatedUser(user, authorities);
    }

    private Collection<GrantedAuthority> buildAuthorities(Users user) {

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRole().name()));

            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getPermission().name()));
            }
        }
        return authorities;
    }
}
