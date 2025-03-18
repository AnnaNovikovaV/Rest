package ru.kata.spring.boot_security.demo.userMapper;


import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.kata.spring.boot_security.demo.model.User;

@Component
public class UserMapper {

    PasswordEncoder passwordEncoder;

    public UserMapper(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toUser(User entity, User updatedUser) {
        entity.setUsername(updatedUser.getUsername());
        entity.setLastname(updatedUser.getLastname());
        entity.setAge(updatedUser.getAge());
        entity.setEmail(updatedUser.getEmail());
        if (!StringUtils.isEmpty(updatedUser.getPassword())) {
            if (!passwordEncoder.matches(updatedUser.getPassword(), entity.getPassword())) {
                entity.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
        }
        entity.setRoles(updatedUser.getRoles());
        return entity;
    }
}
