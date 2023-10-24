package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UsersService;

@Controller
@RequestMapping("/admin") // users - это начальная страница
public class AdminController {
    @Autowired
    private UsersService usersService;

    @GetMapping()
    public String showListOfUsers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("newUser", new User());
        model.addAttribute("users", usersService.showListOfUsers());
        return "admin";
    }

    @GetMapping("/user/{id}")
    public String showUserInfo(@PathVariable("id") long id, Model model){
        model.addAttribute("user", usersService.findUserById(id));
        return "/user";
    }

//    @GetMapping("/admin")
//    public String createUser(Model model) {
//        model.addAttribute("user", new User());
//        return "/new";
//    }

    @PostMapping("/new")
    public String createdUser(@ModelAttribute("user") User user) {

//        user.addRole(new Role("ROLE_USER"));
//        user.addRole(new Role("ROLE_ADMIN"));
        usersService.saveUser(user);

        return "redirect:/admin";
    }

    @PostMapping ("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        usersService.deleteUser(id);
        return "redirect:/admin";
    }

//    @GetMapping("/edit")
//    public String updateUser(@RequestParam("id") long id, Model model) {
//        model.addAttribute("user", usersService.findUserById(id));
//        return "/edit";
//    }

    @PostMapping ("/edit/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") long id) {
        System.out.println(user);
        usersService.updateUser(id, user);
        return "redirect:/admin";
    }
}
