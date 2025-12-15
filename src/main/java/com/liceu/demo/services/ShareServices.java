package com.liceu.demo.services;

import com.liceu.demo.dao.DrawDAO;
import com.liceu.demo.dao.VersionDAO;
import com.liceu.demo.dto.DateGaleryDTO;
import com.liceu.demo.dto.SharedWithCanvas;
import com.liceu.demo.models.Draw;
import com.liceu.demo.models.Share;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShareServices {
    @Autowired
    DrawDAO drawDAO;
    @Autowired
    VersionDAO versionDAO;
    @Autowired
    ShapeServices shapeServices;

    public void saveShares(int idDraw, int idUser, Share.SharePermission permission) {
        drawDAO.saveShare(idDraw, idUser, permission);
    }

    public List<Share> getDrawsSharedWithMe(int idUser) {
        return drawDAO.findSharedWithUser(idUser);
    }

    public List<SharedWithCanvas> transformSharedDraws(List<Share> sharedWithMe) {
        List<SharedWithCanvas> listDrawsData = new ArrayList<>();
        for (Share shared : sharedWithMe) {
            int id = shared.getDrawId();
            Draw draw = drawDAO.getDrawById(id);

            listDrawsData.add(transformToShareDraws(draw, shared.getPermission()));
        }
        return listDrawsData;
    }

    private SharedWithCanvas transformToShareDraws(Draw draw, Share.SharePermission sharePermisions) {
        int idVersion = versionDAO.getVersionForId(draw.getId()).getVersionId();
        SharedWithCanvas shr = new SharedWithCanvas(
                draw.getId(),
                draw.getIdUser(),
                sharePermisions,
                shapeServices.getShapes(idVersion)
        );

        return shr;
    }
}
