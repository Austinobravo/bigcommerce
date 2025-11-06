package com.example.ecommerce.project.repository;

import com.example.ecommerce.project.model.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, String> {
    void deleteAllByRevokedAtBefore(LocalDateTime cutoff);
}
