package com.example.demo.service.impl;

import com.example.demo.model.UserRole;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<UserRole> getAllUsers() {
        return userRoleRepository.findAll();
    }

    @Override
    public void createUser(UserRole user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRoleRepository.saveAndFlush(user);
    }

    @Override
    public void updateUser(Long id, UserRole user) {
        UserRole userDb = userRoleRepository.findById(id).orElse(null);

        if (userDb != null) {
            userDb.setUsername(user.getUsername());
            userDb.setPassword(user.getPassword()); // plaintext password

            userRoleRepository.saveAndFlush(userDb);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRole userrole = userRoleRepository.findByUsername(username);

        if (userrole == null) {
            throw new UsernameNotFoundException("User: " + username + " not found!");
        }
        return userrole;
    }
}
