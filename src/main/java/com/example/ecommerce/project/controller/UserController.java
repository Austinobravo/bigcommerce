package com.example.ecommerce.project.controller;

import com.example.ecommerce.project.dto.UpdateUserDto;
import com.example.ecommerce.project.dto.UpdateUserPasswordDto;
import com.example.ecommerce.project.dto.UserDto;
import com.example.ecommerce.project.model.User;
import com.example.ecommerce.project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {
    private  final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> authenticateUser() {
        User principal = (User) userService.getSignedInUser();
        String email = principal.getEmail();
        User currentUser = userService.findByEmail(email);

        UserDto response = new UserDto();
        response.setId(currentUser.getId());
        response.setEmail(currentUser.getEmail());
        response.setUsername(currentUser.getUsername());
        response.setDeliveryAddress(currentUser.getDeliveryAddress());
        response.setProfileImage(currentUser.getProfileImage());
        return ResponseEntity.ok(response);

    }


    @GetMapping("/all-users")
    public ResponseEntity<List<UserDto>> allUsers(){
        List<UserDto> users = userService.allUsers().stream().map((user)-> {
            UserDto dto = new UserDto();
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setId(user.getId());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PutMapping(value = "/update-user", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateUser ( @RequestPart(value = "data", required = false) UpdateUserDto input, @RequestPart(value = "image", required = false)MultipartFile image){
        User principal = (User) userService.getSignedInUser();
        String Id = principal.getId();

        try{

            User currentUser = userService.updateUserDetails(Id, input, image);

            return ResponseEntity.ok("Updated Successfully");

        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @PatchMapping("/update-password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdateUserPasswordDto input){
        User currentUser = userService.getSignedInUser();
        String Id = currentUser.getId();

        try{
            userService.updateUserPassword(Id, input);
            return ResponseEntity.ok("Updated Successfully");

        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
