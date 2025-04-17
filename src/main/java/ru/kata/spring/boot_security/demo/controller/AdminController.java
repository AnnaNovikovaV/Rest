package ru.kata.spring.boot_security.demo.controller;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {

    UserService userService;

    RoleService roleService;

    private final TemplateEngine templateEngine;

    public AdminController(UserService userService, RoleService roleService, TemplateEngine templateEngine) {
        this.userService = userService;
        this.roleService = roleService;
        this.templateEngine = templateEngine;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String index(Principal principal,
                        HttpServletRequest request,
                        HttpServletResponse response) {
            WebContext context = new WebContext(
                    request,
                    response,
                    request.getServletContext(),
                    request.getLocale(),
                    new HashMap<>()
            );

            context.setVariable("users", userService.findAll());
            context.setVariable("roles", roleService.findAll());
            context.setVariable("newUser", new User());
            context.setVariable("user", userService.getUserByUsername(principal.getName()));

            return templateEngine.process("admin", context);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok("");
    }

    @PatchMapping("/update")
    public ResponseEntity<String> update(@RequestBody User user) {
        userService.update(user);
        return ResponseEntity.ok().body("");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
