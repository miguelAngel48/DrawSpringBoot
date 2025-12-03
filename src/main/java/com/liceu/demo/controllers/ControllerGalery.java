package com.liceu.demo.controllers;


import com.liceu.demo.dto.DateGaleryDTO;
import com.liceu.demo.models.Draw;
import com.liceu.demo.services.DrawServices;
import com.liceu.demo.services.UserServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class ControllerGalery {
    @Autowired
    UserServices userServices;
    @Autowired
    DrawServices drawServices;
    @Autowired
    HttpSession session;

    @GetMapping("/galery")
    public String galery(Model model) {
        model.addAttribute("user", session.getAttribute("user"));
        List<DateGaleryDTO> allPublicsDraws = drawServices.getPublicDraws();
        model.addAttribute("draws",allPublicsDraws);
        return "galery";
    }
    @GetMapping("/privado")
    public String privado(Model model){
        model.addAttribute("user", session.getAttribute("user"));
        int idUser = (int) session.getAttribute("id");
        List<DateGaleryDTO> allUsersDraws = drawServices.getDrawsUser(idUser);
        model.addAttribute("draws",allUsersDraws);
        return "privado";
    }

    @GetMapping("/lienzo")
    public String draw(Model model) {
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("id",session.getAttribute("id"));
        session.getAttribute("id");
        return "lienzo";
    }


}
