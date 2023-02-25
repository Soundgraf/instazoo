package com.example.instazoo.controller;

import com.example.instazoo.payload.request.LoginRequest;
import com.example.instazoo.payload.request.SignupRequest;
import com.example.instazoo.payload.response.JWTTokenSuccessResponse;
import com.example.instazoo.payload.response.MessageResponse;
import com.example.instazoo.security.JWTTokenProvider;
import com.example.instazoo.security.SecurityConstants;
import com.example.instazoo.service.UserService;
import com.example.instazoo.validation.ResponseErrorValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
@RequestMapping("/api/auth")
public class AuthController {

    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ResponseErrorValidation responseErrorValidation;
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest request, BindingResult result) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest request, BindingResult result) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUser(request);
        return ResponseEntity.ok(new MessageResponse("User registry successfully"));
    }
}
