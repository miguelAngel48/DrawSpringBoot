package com.liceu.demo.dao;

import com.liceu.demo.models.Draw;

import java.util.List;

public interface DrawDAO {
    int saveDraw(Draw draw);

    List<Draw> getDrawsUser(int idUser);

    void deleteDraw(int idDraw);

    int getDrawById();

    List<Draw> getDrawsPublics();

    public Draw getDrawForId(int id);

    public List<Draw> getDrawsDeletedUser(int userId);

    public void updateDraw(Draw draw);

    public void updateStatTrash(int idDraw, boolean trash);

    public void updateStatPublic(int idDraw, boolean trash);
}
