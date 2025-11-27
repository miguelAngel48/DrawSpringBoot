package com.liceu.demo.dao;

import com.liceu.demo.models.Draw;

import java.util.List;

public interface DrawDAO {
    void saveDraw( Draw draw);
    List<Draw> getDrawsUser(String user);
    void deleteDraw(String user, int idUser);
    Draw getDrawById(String user, int idDraw);
}
