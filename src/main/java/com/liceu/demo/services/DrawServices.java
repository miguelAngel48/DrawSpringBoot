package com.liceu.demo.services;

import com.liceu.demo.dao.DrawDAO;
import com.liceu.demo.dao.UserDAO;
import com.liceu.demo.dao.VersionDAO;
import com.liceu.demo.dto.DateGaleryDTO;
import com.liceu.demo.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

@Service
public class DrawServices {
    @Autowired
    DrawDAO drawDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    VersionDAO versionDAO;
    @Autowired
    ShapeServices shapeServices;

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

    public Draw getDrawFromId(int idDraw) {
        return drawDAO.getDrawById(idDraw);
    }

    public List<DateGaleryDTO> getDrawsUser(int idUser) {
        List<DateGaleryDTO> allContentGalery = new ArrayList<>();
        for (Draw draw : drawDAO.getDrawsUser(idUser)) {
            allContentGalery.add(transformDrawToDTO(draw));
        }
        return allContentGalery;
    }

    public List<DateGaleryDTO> getPublicDraws() {
        List<DateGaleryDTO> allContentGalery = new ArrayList<>();
        for (Draw draw : drawDAO.getDrawsPublics()) {
            allContentGalery.add(transformDrawToDTO(draw));
        }
        return allContentGalery;
    }

    public List<DateGaleryDTO> getDrawsDeleted(int idUser) {
        List<DateGaleryDTO> allContentGalery = new ArrayList<>();
        for (Draw draw : drawDAO.getDrawsDeletedUser(idUser)) {
            allContentGalery.add(transformDrawToDTO(draw));
        }
        return allContentGalery;
    }

    public DateGaleryDTO transformDrawToDTO(Draw draw) {
        User u = userDAO.getUserById(draw.getIdUser());
        String userName = u.getUsername();
        VersionDraw versionDraw = versionDAO.getVersionForId(draw.getId());

        String jsonShapesData = shapeServices.getShapes(versionDraw.getVersionId());
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

    public void drawInTrash(int idDraw) {
        Draw draw = drawDAO.getDrawForId(idDraw);
        boolean trash = !draw.isTrash();
        drawDAO.updateStatTrash(idDraw, trash);
    }

    public void drawPublicated(int idDraw) {
        Draw draw = drawDAO.getDrawForId(idDraw);
        boolean publicated = !draw.isPublico();
        drawDAO.updateStatPublic(idDraw, publicated);
    }

    public void drawDeleted(int idDraw) {
        drawDAO.deleteDraw(idDraw);
    }

    public DateGaleryDTO getCanvasDTO(int idDraw, int idUser) {

        Draw original = drawDAO.getDrawForId(idDraw);

        if (original == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El dibujo original no existe"
            );
        }
        if (!canCopyDraw(original, idUser)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No tienes permiso para copiar este dibujo"
            );
        }

        Draw copy = getCopyDraw(original, idUser);
        return transformDrawToDTO(copy);
    }

    private boolean canCopyDraw(Draw draw, int idUser) {

        Share.SharePermission permission =
                drawDAO.getPermission(draw.getId(), idUser);

        return permission == Share.SharePermission.EDIT;
    }


    public DateGaleryDTO getDrawDTO(int idDraw, int idUser) {

        Draw draw = drawDAO.getDrawForId(idDraw);
        if (draw == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El dibujo original no existe"
            );
        }
        if (draw.getIdUser() == idUser) return transformDrawToDTO(draw);
        throw new ResponseStatusException(HttpStatus.FORBIDDEN
        );
    }

    private Draw getCopyDraw(Draw original, int newUserId) {

        Draw copy = new Draw();
        copy.setNameDraw(original.getNameDraw() + " (copia)");
        copy.setIdUser(newUserId);
        copy.setWidth(original.getWidth());
        copy.setHeight(original.getHeight());
        copy.setTrash(false);
        copy.setPublico(false);
        copy.setDateCreate(LocalDateTime.now().toString());
        int newDrawId = drawDAO.saveDraw(copy);
        copy.setId(newDrawId);

        VersionDraw newVersion = new VersionDraw();
        newVersion.setDrawid(newDrawId);
        newVersion.setVersionNum(1);
        versionDAO.saveVersion(newVersion);

        VersionDraw savedVersion =
                versionDAO.getVersionForId(newDrawId);

        VersionDraw originalVersion =
                versionDAO.getVersionForId(original.getId());

        shapeServices.copyShapes(
                originalVersion.getVersionId(),
                savedVersion.getVersionId()
        );

        return copy;
    }

    public int copyDraw(int originalDrawId, int idUser) {

        Draw original = drawDAO.getDrawForId(originalDrawId);
        if (original == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El dibujo original no existe"
            );
        }
        if (!canCopyDraw(original, idUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN
            );
        }

        Draw copy = getCopyDraw(original, idUser);
        return copy.getId();
    }

}
