package com.example.instazoo.service;

import com.example.instazoo.enums.Role;
import com.example.instazoo.exception.UserExistsException;
import com.example.instazoo.model.User;
import com.example.instazoo.payload.request.SignupRequest;
import com.example.instazoo.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User createUser(SignupRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.getRoles().add(Role.ROLE_USER);

        try {
            log.info("Saving User: {}", request.getEmail());
            return userRepository.save(user);
        } catch (Exception ex) {
            log.error("Error during registration. {}", ex.getMessage());
            throw new UserExistsException("User " + user.getUsername() + " already exist. Please check creadentials");
        }
    }
}
