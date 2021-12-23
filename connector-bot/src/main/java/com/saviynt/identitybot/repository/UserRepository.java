package com.saviynt.identitybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saviynt.identitybot.model.User;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository < User, Long > {
    User findByEmail(String email);

//    Optional<User> findByEmail(String usernameOrEmail);

    Optional<User> findById(Long id);

    Optional<User> findOneByEmail(String email);

    Optional<User> findByToken(String token);
}