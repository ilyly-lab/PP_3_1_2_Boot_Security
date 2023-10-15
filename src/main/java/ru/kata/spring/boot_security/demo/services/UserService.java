package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    User findByUserName(String name);
    List<User> getAllUsers();
    User getUserBiId(long id);
    void saveUser(User user);
    void updateUser( User user);
    void deleteUser(long id);
}
