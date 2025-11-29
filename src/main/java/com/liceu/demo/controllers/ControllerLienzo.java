package com.liceu.demo.controllers;

import com.liceu.demo.dto.CanvasClientDTO;
import com.liceu.demo.services.DrawServices;
import com.liceu.demo.services.VersionDrawServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/lienzo")
public class ControllerLienzo {
@Autowired
    DrawServices drawServices;
@Autowired
    VersionDrawServices versionDrawServices;
    @PostMapping("/save")
    public String saveDraw(Model model,   @RequestBody CanvasClientDTO fetchData) {
        System.out.println(fetchData);
       drawServices.saveDrawOnDataBase(fetchData.id(),fetchData.drawName(),fetchData.idUser(),fetchData.width(),fetchData.height(),fetchData.trash());
       versionDrawServices.saveVersionOnDataBase(fetchData.idUser());
        return "lienzo";
    }

}
