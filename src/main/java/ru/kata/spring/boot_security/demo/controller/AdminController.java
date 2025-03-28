package ru.kata.spring.boot_security.demo.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminController {

    UserService userService;

    RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String index(Model model, Principal principal) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("newUser", new User());
        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        return "admin";
    }

    @GetMapping("/getAllUsers")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll()); //работает!
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> create(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok("");
    }

//    @PatchMapping("/edit")
//    public String edit(Model model, @RequestParam("id") int id) {
//        model.addAttribute("user", userService.findForEdit(id));
//        return "admin";
//    }

    @PatchMapping("/update")
    @ResponseBody
    public ResponseEntity<String> update(@RequestBody User user) {
//        userService.findForEdit(id);
        userService.update(user);
        return ResponseEntity.ok().body(""); //работает!
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.noContent().build(); //работает
    }
}
