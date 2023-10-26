package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RolesService;
import ru.kata.spring.boot_security.demo.services.UsersService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Controller
@RequestMapping("/admin") // users - это начальная страница
public class AdminController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private RolesService rolesService;
    @Autowired
    EntityManager entityManager;

    @GetMapping()
    public String showListOfUsers(Model model) {
        List<Role> listOfRoles = entityManager.createQuery("from Role").getResultList();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        model.addAttribute("listOfRoles", listOfRoles);
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        model.addAttribute("users", usersService.showListOfUsers());
        return "admin";
    }

    @PostMapping("/new")
    public String createdUser(@ModelAttribute("newUser") User newUser,
                              @ModelAttribute("thisRole") Long roleId) {
        Role role = rolesService.findRoleById(roleId);

        newUser.addRole(role);
        role.getUsers().add(newUser);

        usersService.saveUser(newUser);

        return "redirect:/admin";
    }

    @PostMapping ("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        usersService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping ("/edit/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @ModelAttribute("thisRole") Long roleId) {

        Role role = rolesService.findRoleById(roleId);

        role.getUsers().remove(user);

        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        role.getUsers().add(user);

        usersService.updateUser(user);
        return "redirect:/admin";
    }
}
