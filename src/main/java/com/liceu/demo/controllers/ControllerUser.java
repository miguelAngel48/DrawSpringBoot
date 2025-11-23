package com.liceu.demo.controllers;

import com.liceu.demo.dao.UserDAO;
import com.liceu.demo.models.User;
import com.liceu.demo.services.UserServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControllerUser {
    @Autowired
    UserServices userServices;
    HttpSession session;
    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model, @RequestParam String name, @RequestParam String password,
                           @RequestParam String username, @RequestParam String passwordCheck) {
        if (userServices.saveUser(name, password, username, passwordCheck)) return "redirect:/login";
        model.addAttribute("message","check again the password, it must be longer than 5 characters");
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        session.invalidate();
        return "login";
    }

    @PostMapping("/login")
    public String logincheck(Model model, @RequestParam String username, @RequestParam String password) {
        if (userServices.checkLogin(username, password)) {
            session.setAttribute("user",username);
            return "redirect:/galery";
        } else {
            model.addAttribute("message", "The user does not exist or the password are invalid ");
            return "login";
        }
    }
}