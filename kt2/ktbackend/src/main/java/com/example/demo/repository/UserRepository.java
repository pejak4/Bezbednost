package com.example.demo.repository;

import com.example.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findOneByEmailAndPassword(String email, String password);
    Users findOneByEmail(String email);

}
