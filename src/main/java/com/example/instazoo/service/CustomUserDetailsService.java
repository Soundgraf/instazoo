package com.example.instazoo.service;

import com.example.instazoo.model.User;
import com.example.instazoo.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Email not found with username: " + username));
        return build(user);
    }

    public User loadUserById(Long id) {
        return userRepository.findUserById(id).orElse(null);
    }

    public static User build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.name()))
            .collect(Collectors.toList());
        return User.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .authorities(authorities)
            .build();
    }
}
