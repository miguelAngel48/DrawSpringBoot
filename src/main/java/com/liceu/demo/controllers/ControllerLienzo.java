package com.liceu.demo.controllers;

import com.liceu.demo.dto.CanvasClientDTO;
import com.liceu.demo.dto.DataVersionsDTO;
import com.liceu.demo.dto.DateGaleryDTO;
import com.liceu.demo.models.Draw;
import com.liceu.demo.models.VersionDraw;
import com.liceu.demo.services.DrawServices;
import com.liceu.demo.services.ShapeServices;
import com.liceu.demo.services.VersionDrawServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/lienzo")
public class ControllerLienzo {
    @Autowired
    DrawServices drawServices;
    @Autowired
    VersionDrawServices versionDrawServices;
    @Autowired
    ShapeServices shapeServices;
    @Autowired
    HttpSession session;

    @PostMapping("/save")
    public ResponseEntity<Integer> saveDraw(Model model, @RequestBody CanvasClientDTO fetchData) {
        Draw draw = drawServices.saveDrawOnDataBase(fetchData.id(), fetchData.drawName(), fetchData.idUser(),
                fetchData.width(), fetchData.height(), fetchData.trash(),fetchData.publico());
        System.out.println(draw.getId());
        versionDrawServices.saveVersionOnDataBase(draw.getId());
        int idLastVersion = versionDrawServices.getVersionId(draw.getId());
        shapeServices.saveShapes(idLastVersion, fetchData.jsonShapes());
        return ResponseEntity.ok(draw.getId());
    }

    @GetMapping("/get/{id}")
    public String getDraw(@PathVariable int id, Model model) {

        model.addAttribute("user", session.getAttribute("user"));
        int iduser = (int) session.getAttribute("id");

        try {
            DateGaleryDTO dto = drawServices.getDrawDTO(id, iduser);

            model.addAttribute("drawData", dto.jsonShapes());
            model.addAttribute("drawName", dto.drawName());
            model.addAttribute("idDraw", dto.id());
            model.addAttribute("width", dto.width());
            model.addAttribute("height", dto.height());

            return "lienzo";

        } catch (ResponseStatusException ex) {

            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                model.addAttribute("message", ex.getReason());
                return "404";
            }

            if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                model.addAttribute("message", ex.getReason());
                return "403";
            }

            throw ex;
        }
    }


    @GetMapping("/preview/{id}")
    public String previewDraw(@PathVariable int id, Model model) {
        int idUser = (int)session.getAttribute("id");
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("id",idUser);
        DateGaleryDTO dto = drawServices.getDrawDTO(id,idUser);
        model.addAttribute("drawData",dto.jsonShapes());
        model.addAttribute("drawName",dto.drawName());
        model.addAttribute("idDraw",dto.id());
        model.addAttribute("width",dto.width());
        model.addAttribute("height",dto.height());
        return "previewDraw";
    }
    @GetMapping("/copy/{id}")
    public String copyDraw(@PathVariable int id, Model model) {

        int iduser = (int) session.getAttribute("id");
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("id", iduser);

        try {

            DateGaleryDTO dto = drawServices.getCanvasDTO(id, iduser);


            int newDrawId = drawServices.copyDraw(id, iduser);

            return "redirect:/lienzo/get/" + newDrawId;

        } catch (ResponseStatusException ex) {

            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                model.addAttribute("message", ex.getReason());
                return "404";
            }

            if (ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                model.addAttribute("message", ex.getReason());
                return "403";
            }

            throw ex;
        }
    }




}
