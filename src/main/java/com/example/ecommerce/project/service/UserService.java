package com.example.ecommerce.project.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ecommerce.project.dto.UpdateUserDto;
import com.example.ecommerce.project.dto.UpdateUserPasswordDto;
import com.example.ecommerce.project.model.User;
import com.example.ecommerce.project.repository.UserRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private Cloudinary cloudinary;

    public UserService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, Cloudinary cloudinary) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cloudinary = cloudinary;
    }

    public List<User> allUsers(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDetails loadUserByEmailOrUsername (String identifier){
        return userRepository.findUserByEmail(identifier)
                .or(() ->  userRepository.findUserByUsername(identifier))
                .map(user -> new User(user.getEmail(), user.getPassword(), user.getDeliveryAddress(), user.getProfileImage(), String.valueOf(new ArrayList<>()))
                ).orElseThrow(() -> new UsernameNotFoundException("User not found " + identifier ));
    }

    public User updateUserDetails(String userId, UpdateUserDto input, MultipartFile image) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (input != null) {
            if(input.getPassword() != null && !input.getPassword().isEmpty()){
                user.setPassword(passwordEncoder.encode(input.getPassword()));
            }

        }

        if(image != null && !image.isEmpty()){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(image.getInputStream())
                    .size(600, 600)
                    .outputQuality(0.7)
                    .toOutputStream(outputStream);
         byte[] compressedImage = outputStream.toByteArray();

         Map uploadResult = cloudinary.uploader().upload(compressedImage, ObjectUtils.asMap(
                 "folder", "users/"+ userId,
                 "resource_type", "image"
         ));

         String imageUrl = uploadResult.get("secure_url").toString();
         user.setProfileImage(imageUrl);
        }

        return userRepository.save(user);

    }

    public void updateUserPassword(String userId, UpdateUserPasswordDto input){

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if(!passwordEncoder.matches(input.getCurrentPassword(), user.getPassword())){
            throw new IllegalArgumentException("Password does not match");
        }

        user.setPassword(passwordEncoder.encode(input.getNewPassword()));
        userRepository.save(user);
    }

    public User getSignedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

}
