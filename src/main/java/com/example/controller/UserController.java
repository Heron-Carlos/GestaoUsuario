package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;

import java.util.List;

public class UserController {
    private final UserRepository userRepository;

    public UserController() {
        this.userRepository = new UserRepository();
    }

    public void saveUser(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
    }

    public void updateUser(User user, String name, String email, String password) {
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.update(user);
    }

    public void deleteUser(String id) {
        userRepository.delete(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        return userRepository.findById(new org.bson.types.ObjectId(id));
    }
}
