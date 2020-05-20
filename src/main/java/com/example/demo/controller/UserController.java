package com.example.demo.controller;

import com.example.demo.model.UserRole;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello everyone!";
    }

    @GetMapping
    public List<UserRole> getAllUsers() {
        return userService.getAllUsers();
    }

    // add user
    @GetMapping("/create")
    public void createUserByUsernamePassword(String username,
                                             String password) {
        UserRole userrole = new UserRole();
        userrole.setPassword(password);
        userrole.setUsername(username);

        userService.createUser(userrole);
    }

    @PostMapping
    public void createUser(@RequestBody UserRole userrole) {
        System.out.println("UserController.createUser");
        System.out.println("user = " + userrole);

        userService.createUser(userrole);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id,
                           @RequestBody UserRole userrole) {

        System.out.println("UserController.updateUser");
        System.out.println("id = " + id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication.getName() = " + authentication.getName());

        userService.updateUser(id, userrole);
    }
}