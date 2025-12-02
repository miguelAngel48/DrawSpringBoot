package com.liceu.demo.dao;

import com.liceu.demo.models.Draw;

import java.util.List;

public interface DrawDAO {
    int saveDraw( Draw draw);
    List<Draw> getDrawsUser(String user);
    void deleteDraw(String user, int idUser);
    int getDrawById();
     List<Draw> getDrawsPublics();
}
