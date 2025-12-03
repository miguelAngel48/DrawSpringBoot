package com.liceu.demo.services;

import com.liceu.demo.dao.DrawDAO;
import com.liceu.demo.dao.UserDAO;
import com.liceu.demo.dto.CanvasClientDTO;
import com.liceu.demo.dto.DateGaleryDTO;
import com.liceu.demo.models.Draw;
import com.liceu.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;

@Service
public class DrawServices {
    @Autowired
    DrawDAO drawDAO;
    @Autowired
    UserDAO userDAO;

    public Draw saveDrawOnDataBase(int id, String nameDraw, int idUser, int width, int height, boolean trash, boolean publico) {
        Draw draw = new Draw();
        draw.setId(id);
        draw.setNameDraw(nameDraw);
        draw.setIdUser(idUser);
        draw.setWidth(width);
        draw.setHeight(height);
        draw.setTrash(trash);
        int idDraw = drawDAO.saveDraw(draw);
        draw.setId(idDraw);
        draw.setPublico(publico);
        return draw;
    }

    public int getLastId() {
        return drawDAO.getDrawById();
    }
    public List<DateGaleryDTO> getDrawsUser(int idUser) {
        List<DateGaleryDTO> allContentGalery = new ArrayList<>();
        for ( Draw draw :drawDAO.getDrawsUser(idUser)){
            allContentGalery.add(transformDrawToDTO(draw));
        }
        return allContentGalery;
    }
public List<DateGaleryDTO> getPublicDraws(){
    List<DateGaleryDTO> allContentGalery = new ArrayList<>();
    for ( Draw draw :drawDAO.getDrawsPublics()){
        allContentGalery.add(transformDrawToDTO(draw));
    }
        return allContentGalery;
}

    private DateGaleryDTO transformDrawToDTO(Draw draw) {
         User u = userDAO.getUserById(draw.getIdUser());
        String userName = u.getUsername();
        String jsonShapesData = "";
        DateGaleryDTO dto = new DateGaleryDTO(
                draw.getId(),
                jsonShapesData,
                draw.getNameDraw(),
                userName,
                draw.getWidth(),
                draw.getHeight(),
                draw.isTrash(),
                draw.isPublico(),
                draw.getDateCreate().toString()

        );

        return dto;
    }
}
