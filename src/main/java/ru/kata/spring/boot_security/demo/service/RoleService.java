package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();
//    Role findById(Long id);
//    void save(Role role);
//    void deleteById(Long id);
    Role getRoleByName(String name);
}
