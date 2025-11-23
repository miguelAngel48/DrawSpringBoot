package com.liceu.demo.controllers;

import com.liceu.demo.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerGalery {
    @Autowired
    UserServices userServices;
    @GetMapping("/galery")
    public String galery (){
        return "galery";
    }
}
