package com.auth.service.authservice.security;

import com.auth.service.authservice.model.User;
import com.auth.service.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // This method is called by Spring Security when someone tries to login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // Find user by username or email (flexible login)
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Check if user account is active
        if (!user.getIsActive()) {
            throw new RuntimeException("User account is disabled");
        }

        // Update last login time
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Convert our User entity to Spring Security's UserDetails
        return new CustomUserDetails(user);
    }

    // Custom UserDetails implementation
    public static class CustomUserDetails implements UserDetails {
        private final User user;

        public CustomUserDetails(User user) {
            this.user = user;
        }

        // Return user authorities/roles (what they can do)
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authorities = new ArrayList<>();
            // Add role with ROLE_ prefix (Spring Security convention)
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
            return authorities;
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getUsername();
        }

        // Account status checks
        @Override
        public boolean isAccountNonExpired() {
            return true; // We're not implementing account expiration
        }

        @Override
        public boolean isAccountNonLocked() {
            return true; // We're not implementing account locking
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true; // We're not implementing credential expiration
        }

        @Override
        public boolean isEnabled() {
            return user.getIsActive(); // Account is enabled if active
        }

        // Helper method to get the actual User entity
        public User getUser() {
            return user;
        }
    }
}

