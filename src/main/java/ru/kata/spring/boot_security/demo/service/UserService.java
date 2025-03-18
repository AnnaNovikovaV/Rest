package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User getUserByUsername(String username);

    void save(User user);

    User findForEdit(int id);

    void delete(int id);

    void update(User updateUser);


}
