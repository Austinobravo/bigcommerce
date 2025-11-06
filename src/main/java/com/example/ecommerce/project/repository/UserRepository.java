package com.example.ecommerce.project.repository;

import com.example.ecommerce.project.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByVerificationCode(String verificationCode);
    Optional<User> findUserByUsername(String username);
}
