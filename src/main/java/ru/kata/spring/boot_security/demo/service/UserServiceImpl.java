package ru.kata.spring.boot_security.demo.service;


import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.userMapper.UserMapper;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    UserRepository repository;
    RoleService roleService;
    PasswordEncoder passwordEncoder;
    UserMapper mapper;

    public UserServiceImpl(UserRepository repository, RoleService roleService, UserMapper mapper, @Lazy PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleService = roleService;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User findForEdit(int id) {
        User user = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword("");
        return user;
    }

    public User findById(int id) {
        User user = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRoles(user));
        repository.save(user);
    }

    @Transactional
    public void update(User updatedUser) {
        User user = findById(updatedUser.getId());
        List<Role> roles = updatedUser.getRoles().stream().map(r -> getRole(r.getName())).collect(Collectors.toList());
        updatedUser.setRoles(roles);
        repository.save(mapper.toUser(user, updatedUser));
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

    private Role getRole(String name) {
        Role role = roleService.getRoleByName(name);
        return role;
    }

    private List<Role> getRoles(User user) {
        List<Long> ids = user.getRoles()
                .stream()
                .map(Role::getId)
                .collect(Collectors.toList());
        return ids.stream()
                .map(id -> roleService.findById(id))
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void initDefaultUsers() {
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        roleService.saveAll(List.of(userRole, adminRole));

        User admin = new User();
        admin.setUsername("admin");
        admin.setLastname("admin");
        admin.setAge(30);
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRoles(List.of(adminRole));

        User user = new User();
        user.setUsername("user");
        user.setLastname("user");
        user.setAge(20);
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRoles(List.of(userRole));

        repository.saveAll(List.of(admin, user));
    }
}
