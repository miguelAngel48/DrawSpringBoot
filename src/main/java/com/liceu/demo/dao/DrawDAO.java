package com.liceu.demo.dao;

import com.liceu.demo.models.Draw;
import com.liceu.demo.models.Share;

import java.util.List;

public interface DrawDAO {
    int saveDraw(Draw draw);

    List<Draw> getDrawsUser(int idUser);

    void deleteDraw(int idDraw);

    public Draw getDrawById(int idDraw);

    List<Draw> getDrawsPublics();

    public Draw getDrawForId(int id);

    public List<Draw> getDrawsDeletedUser(int userId);

    public void updateDraw(Draw draw);

    public void updateStatTrash(int idDraw, boolean trash);

    public void updateStatPublic(int idDraw, boolean trash);

    public void saveShare(int idDraw, int idUser, Share.SharePermission permission);

    public List<Share> findSharedByUser(int ownerId);

    public boolean isDrawSharedWithUser(int drawId, int userId);

    public List<Share> findSharedWithUser(int userId);

    public Share.SharePermission getPermission(int drawId, int userId);
}
