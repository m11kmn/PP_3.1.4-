package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public List<User> showListOfUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElse(null);
    }

    @Transactional
    public void saveUser(User user, Long roleId) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleService.findRoleById(roleId);
        user.addRole(role);
        role.getUsers().add(user);

        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User updatedUser, Long roleId) {
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        Role role = roleService.findRoleById(roleId);
        role.getUsers().remove(updatedUser);
        updatedUser.setRoles(new HashSet<>(Collections.singletonList(role)));
        role.getUsers().add(updatedUser);

        userRepository.save(updatedUser);

    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
