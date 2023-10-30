package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping
    public String showListOfUsers(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("listOfRoles", roleService.showListOfRoles());
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        model.addAttribute("users", userService.showListOfUsers());
        return "admin";
    }

    @PostMapping("/new")
    public String createdUser(@ModelAttribute("newUser") User newUser, @ModelAttribute("thisRole") Long roleId) {
        userService.saveUser(newUser, roleId);
        return "redirect:/admin";
    }

    @PostMapping ("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping ("/edit/{id}")
    public String updateUser(@ModelAttribute("user") User user, @ModelAttribute("thisRole") Long roleId) {
        userService.updateUser(user, roleId);
        return "redirect:/admin";
    }
}
