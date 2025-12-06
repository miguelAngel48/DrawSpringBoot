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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        Draw draw = drawServices.saveDrawOnDataBase(fetchData.id(), fetchData.drawName(), fetchData.idUser(), fetchData.width(), fetchData.height(), fetchData.trash(),fetchData.publico());
        System.out.println(draw.getId());
        versionDrawServices.saveVersionOnDataBase(draw.getId());
        int idLastVersion = versionDrawServices.getVersionId(draw.getId());
        shapeServices.saveShapes(idLastVersion, fetchData.jsonShapes());
        return ResponseEntity.ok(draw.getId());
    }

    @GetMapping("/get/{id}")
    public String getDraw(@PathVariable int id, Model model){
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("id",session.getAttribute("id"));
        DateGaleryDTO dto = drawServices.getCanvasDTO(id);
        model.addAttribute("drawData",dto.jsonShapes());
        model.addAttribute("drawName",dto.drawName());
        model.addAttribute("idDraw",dto.id());
        model.addAttribute("width",dto.width());
        model.addAttribute("height",dto.height());
        List<DataVersionsDTO> versionsDrawList = versionDrawServices.getAllVersions(dto.id());
        model.addAttribute("versions", versionsDrawList);
        return "lienzo";
    }
    @GetMapping("/preview/{id}")
    public String previewDraw(@PathVariable int id, Model model) {
        model.addAttribute("user", session.getAttribute("user"));
        model.addAttribute("id",session.getAttribute("id"));
        DateGaleryDTO dto = drawServices.getCanvasDTO(id);
        model.addAttribute("drawData",dto.jsonShapes());
        model.addAttribute("drawName",dto.drawName());
        model.addAttribute("idDraw",dto.id());
        model.addAttribute("width",dto.width());
        model.addAttribute("height",dto.height());
        return "previewDraw";
    }

}
