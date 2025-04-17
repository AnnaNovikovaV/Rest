package ru.kata.spring.boot_security.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(String username);

    @Query(value = "SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);
}
