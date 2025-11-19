package com.example.ecommerce.project.service;

import com.example.ecommerce.project.model.AuthProvider;
import com.example.ecommerce.project.model.User;
import com.example.ecommerce.project.repository.UserRepository;
import com.example.ecommerce.project.responses.LoginResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    @Value("${google.client-id}")
    private final String googleClientId;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public LoginResponse verifyGoogleToken(String idToken){
        try{
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                    .Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList((googleClientId)))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if(googleIdToken == null){
                throw new RuntimeException("Invalid Id Token");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            String email = payload.getEmail();
            String name = payload.get("name").toString();
            String picture = (String) payload.get("picture");

            User user = userRepository.findUserByEmail(email).orElseGet(() -> registerFromGoogle(email,name, picture));

            String jwt = jwtService.generateToken(user);

            return  new LoginResponse(jwt, jwtService.getExpirationTime());

        }
        catch (RuntimeException | GeneralSecurityException | IOException e) {
            throw new RuntimeException("Google authentication failed: " + e.getMessage());
        }
    }

    private User registerFromGoogle(String email, String name, String picture){
        User newUser = new User();
        newUser.setProfileImage(picture);
        newUser.setUsername(email.split("@")[0]);
        newUser.setPassword(passwordEncoder.encode(email));
        newUser.setEnabled(true);
        newUser.setAuthProvider(AuthProvider.GOOGLE);

        return userRepository.save(newUser);
    }

}
