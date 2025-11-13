package com.example.ecommerce.project.service;

import com.example.ecommerce.project.dto.LoginUserDto;
import com.example.ecommerce.project.dto.RegisterUserDto;
import com.example.ecommerce.project.dto.VerifyUserDto;
import com.example.ecommerce.project.model.User;
import com.example.ecommerce.project.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public User signup(RegisterUserDto input){
        Optional<User> existingEmail = userRepository.findUserByEmail(input.getEmail().toLowerCase());
        Optional<User> existingUsername = userRepository.findUserByEmail(input.getUsername().toLowerCase());
        if(existingEmail.isPresent() || existingUsername.isPresent()){
            throw new RuntimeException("This user already exists");
        }

        User user = new User(input.getUsername().toLowerCase(), input.getEmail().toLowerCase(), passwordEncoder.encode(input.getPassword()), input.getDeliveryAddress(), input.getProfileImage());

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input){
        User user = userRepository.findUserByEmail(input.getEmail().toLowerCase()).orElseThrow(()-> new RuntimeException("User not found"));

        if(!user.isEnabled()) {
            throw new RuntimeException("Account not verified");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail().toLowerCase(), input.getPassword()));
        return user;
        }

    public void verifyUser(VerifyUserDto input){
        Optional <User> optionalUser = userRepository.findUserByEmail(input.getEmail().toLowerCase());
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getVerificationExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }

            if(user.getVerificationCode().equals(input.getVerificationCode())) {
             user.setEnabled(true);
             user.setVerificationCode(null);
             user.setVerificationExpiresAt(null);

             userRepository.save(user);
            }else {
                throw new RuntimeException("Invalid verification code");
            }

        }else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email){
        Optional<User> optionalUser = userRepository.findUserByEmail(email.toLowerCase());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.isEnabled()){
                throw new RuntimeException("Account already verified");
            }else{
                user.setVerificationCode(generateVerificationCode());
                user.setVerificationExpiresAt(LocalDateTime.now().plusHours(1));
                sendVerificationEmail(user);
                userRepository.save(user);
            }
        }else{
            throw new RuntimeException("If this user exists, they will get a mail");
        }

    }

    public void sendVerificationEmail(User user){
        String subject = "Account verification";
        Map<String, Object> variables = Map.of(
                "username", user.getUsername(),
                "verificationCode", user.getVerificationCode(),
                "verificationLink", "http://localhost:3000/verify-email?email="+user.getEmail()
        );
        String verificationCode = user.getVerificationCode();
        try{
            emailService.sendMail(user.getEmail(), subject, "verification-email",  variables);

        }catch(MessagingException e){
            e.printStackTrace();

        }
    }
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(999999) + 100000;
        return String.valueOf(code);
    }
}




