package com.saviynt.identitybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saviynt.identitybot.model.User;


@Repository
public interface EPMAccountRepository extends JpaRepository<User, Long> {

}
