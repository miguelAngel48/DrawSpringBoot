package com.liceu.demo.services;

import com.liceu.demo.dao.DrawDAO;
import com.liceu.demo.models.Draw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrawServices {
    @Autowired
    DrawDAO drawDAO;
public void saveDrawOnDataBase(int id,String nameDraw,int idUser, int width, int height, boolean trash ){
    Draw draw = new Draw();

    draw.setNameDraw(nameDraw);
    draw.setIdUser(idUser);
    draw.setWidth(width);
    draw.setHeight(height);
    draw.setTrash(trash);
    drawDAO.saveDraw(draw);
}

}
