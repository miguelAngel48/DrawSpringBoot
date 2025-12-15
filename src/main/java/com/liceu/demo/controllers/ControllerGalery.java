package com.liceu.demo.controllers;


import com.liceu.demo.dto.DateGaleryDTO;
import com.liceu.demo.dto.SharedWithCanvas;
import com.liceu.demo.models.Draw;
import com.liceu.demo.models.Share;
import com.liceu.demo.models.User;
import com.liceu.demo.services.DrawServices;
import com.liceu.demo.services.ShareServices;
import com.liceu.demo.services.UserServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;


@Controller
public class ControllerGalery {
    @Autowired
    UserServices userServices;
    @Autowired
    DrawServices drawServices;
    @Autowired
    HttpSession session;
    @Autowired
    ShareServices shareServices;

    @GetMapping("/galery")
    public String galery(Model model) {
        model.addAttribute("user", session.getAttribute("user"));
        List<DateGaleryDTO> allPublicsDraws = drawServices.getPublicDraws();
        model.addAttribute("draws",allPublicsDraws);

        return "galery";
    }
    @PostMapping("/delete")
    public String deleteDraw(@RequestParam int id){
        drawServices.drawInTrash(id);
        return "redirect:/privado";
    }
    @PostMapping("/trash")
    public String deleteDrawPermanent(Model model,@RequestParam int id){
        drawServices.drawDeleted(id);
        return "redirect:/trash";
    }
    @PostMapping("publicated")
    public String PublicateDraws(@RequestParam int id){
        drawServices.drawPublicated(id);
        return "redirect:/privado";
    }
    @PostMapping("/share")
    public String ShareDrawsWithMe (@RequestParam int id, @RequestParam int idUser, @RequestParam Share.SharePermission permission) {
        User owner = userServices.getUser((String) session.getAttribute("user"));
        Draw draw = drawServices.getDrawFromId(id);
        if (draw.getIdUser() != (owner.getId())) {
            return "redirect:/privado?error=noPermission";
        }
        shareServices.saveShares(id, idUser, permission);
     return "redirect:/privado";
    }

    @PostMapping("/outTrash")
    public String getOutDrawOfTheTrash(@RequestParam int id){
        drawServices.drawInTrash(id);
        return "redirect:/trash";
    }
    @GetMapping("/trash")
    public String deletePage(Model model){
        session.getAttribute("id");
        model.addAttribute("user", session.getAttribute("user"));
        int idUser = (int) session.getAttribute("id");
        List<DateGaleryDTO> allUsersDraws = drawServices.getDrawsDeleted(idUser);
        model.addAttribute("draws",allUsersDraws);
        return "trash";
    }
    @GetMapping("/privado")
    public String privado(Model model){
        model.addAttribute("user", session.getAttribute("user"));
        int idUser = (int) session.getAttribute("id");
        List<DateGaleryDTO> allUsersDraws = drawServices.getDrawsUser(idUser);
        model.addAttribute("draws",allUsersDraws);
        //shared with me
        List<Share> sharedWithMe = shareServices.getDrawsSharedWithMe((int) session.getAttribute("id"));
        List<SharedWithCanvas> sharedData = shareServices.transformSharedDraws(sharedWithMe);
        model.addAttribute("sharedWithMe", sharedData);
        return "privado";
    }

    @GetMapping("/lienzo")
    public String draw(Model model) {
        session.getAttribute("id");
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("id",session.getAttribute("id"));
        return "lienzo";
    }


}
