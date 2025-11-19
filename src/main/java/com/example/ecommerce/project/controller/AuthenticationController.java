package com.example.ecommerce.project.controller;

import com.example.ecommerce.project.dto.GoogleLoginDto;
import com.example.ecommerce.project.dto.LoginUserDto;
import com.example.ecommerce.project.dto.RegisterUserDto;
import com.example.ecommerce.project.dto.VerifyUserDto;
import com.example.ecommerce.project.model.RevokedToken;
import com.example.ecommerce.project.model.User;
import com.example.ecommerce.project.repository.RevokedTokenRepository;
import com.example.ecommerce.project.responses.LoginResponse;
import com.example.ecommerce.project.service.AuthenticationService;
import com.example.ecommerce.project.service.GoogleAuthService;
import com.example.ecommerce.project.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private  final AuthenticationService authenticationService;
    private final RevokedTokenRepository revokedTokenRepository;
    private final GoogleAuthService googleAuthService;


    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, RevokedTokenRepository revokedTokenRepository, GoogleAuthService googleAuthService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.revokedTokenRepository = revokedTokenRepository;
        this.googleAuthService = googleAuthService;
    }


    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDto registerUserDto){
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginUserDto loginUserDto){
        User authenticateUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticateUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/google/login")
    public ResponseEntity<LoginResponse> googleLogin(@Valid @RequestBody GoogleLoginDto googleLoginDto){
        LoginResponse loginResponse = googleAuthService.verifyGoogleToken(googleLoginDto.getIdToken());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify-user")
    public ResponseEntity<?> verifyUser(@Valid @RequestBody VerifyUserDto verifyUserDto){
        try{
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

    @PostMapping("/resend-code")
    public ResponseEntity<?> resendVerificationCode(@RequestParam("email") String email){
        try{
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Successful");
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String header){
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }

        String token = header.substring(7);
        if(token.isEmpty()){
            return ResponseEntity.badRequest().body("Please provide a token");
        }
        revokedTokenRepository.save(new RevokedToken(token, LocalDateTime.now()));

        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        revokedTokenRepository.deleteAllByRevokedAtBefore(cutoff);

        return ResponseEntity.ok("Successfully logged out");
    }
}
