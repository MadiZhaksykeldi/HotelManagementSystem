package com.example.demo.service;

import com.example.demo.model.UserRole;

import java.util.List;

public interface UserService {

    List<UserRole> getAllUsers();
    void createUser(UserRole userrole);
    void updateUser(Long id, UserRole userrole);
}
