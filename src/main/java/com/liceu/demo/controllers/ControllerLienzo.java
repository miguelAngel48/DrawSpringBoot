package com.liceu.demo.controllers;

import com.liceu.demo.dto.CanvasClientDTO;
import com.liceu.demo.models.Draw;
import com.liceu.demo.services.DrawServices;
import com.liceu.demo.services.ShapeServices;
import com.liceu.demo.services.VersionDrawServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lienzo")
public class ControllerLienzo {
    @Autowired
    DrawServices drawServices;
    @Autowired
    VersionDrawServices versionDrawServices;
    @Autowired
    ShapeServices shapeServices;

    @PostMapping("/save")
    public ResponseEntity<Integer> saveDraw(Model model, @RequestBody CanvasClientDTO fetchData) {
        Draw draw = drawServices.saveDrawOnDataBase(fetchData.id(), fetchData.drawName(), fetchData.idUser(), fetchData.width(), fetchData.height(), fetchData.trash(),fetchData.publico());
        System.out.println(draw.getId());
        versionDrawServices.saveVersionOnDataBase(draw.getId());
        int idLastVersion = versionDrawServices.getVersionId(draw.getId());
        shapeServices.saveShapes(idLastVersion, fetchData.jsonShapes());
        return ResponseEntity.ok(draw.getId());
    }

    @GetMapping("get")
    public String getDrawTemplate(Model model){
        return "get";
    }

}
