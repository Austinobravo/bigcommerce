package com.example.ecommerce.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String Id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @JsonIgnore
    private AuthProvider authProvider;

    @JsonIgnore
    @Column(name = "verification_code")
    private String verificationCode;

    @JsonIgnore()
    @Column(name = "verification_expires_at")
    private LocalDateTime verificationExpiresAt;

    @JsonIgnore()
    private boolean enabled = false;

    private String deliveryAddress;

    private String profileImage;


    public User(String username, String email, String password, String deliveryAddress, String profileImage) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.deliveryAddress = deliveryAddress;
        this.profileImage = profileImage;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
