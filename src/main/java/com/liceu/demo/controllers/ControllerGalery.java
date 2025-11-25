package com.liceu.demo.controllers;


import com.liceu.demo.services.UserServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControllerGalery {
    @Autowired
    UserServices userServices;
    @Autowired
    HttpSession session;

    @GetMapping("/galery")
    public String galery (Model model){
        model.addAttribute("user",session.getAttribute("user"));
        return "galery";
    }



    @GetMapping("/lienzo")
    public String draw (){
        return "lienzo";
    }
}
