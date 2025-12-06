package com.liceu.demo.services;

import com.liceu.demo.dao.VersionDAO;
import com.liceu.demo.dto.DataVersionsDTO;
import com.liceu.demo.models.Shape;
import com.liceu.demo.models.VersionDraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VersionDrawServices {
    @Autowired
    VersionDAO versionDAO;
    @Autowired
    ShapeServices shape;

    public void saveVersionOnDataBase(int drawId) {
        VersionDraw ver = new VersionDraw();
        ver.setDrawid(drawId);
        ver.setVersionNum(versionDAO.findMaxVersionNumByDrawId(drawId) + 1);
        versionDAO.saveVersion(ver);
    }

    public int getVersionId(int drawId) {
        VersionDraw v = versionDAO.getVersionForId(drawId);
        return v.getVersionId();
    }
    public List<DataVersionsDTO> getAllVersions(int drawId) {
        List<VersionDraw> v = versionDAO.getAllVersionForId(drawId);
        List<DataVersionsDTO> versionsDTO = transformShapesInData(v);
        return versionsDTO;
    }

    private List<DataVersionsDTO> transformShapesInData(List<VersionDraw> listVersions) {
        List<DataVersionsDTO> listDataVersions = new ArrayList<>();
        for (VersionDraw version : listVersions ){
            DataVersionsDTO data = versionDataTransfer(version);
            listDataVersions.add(data);
        }
        return listDataVersions;
    }

    private DataVersionsDTO versionDataTransfer(VersionDraw version) {
        String shapes = shape.getShapes(version.getVersionId());
        DataVersionsDTO data = new DataVersionsDTO( version.getVersionId(),
                    version.getDrawid(),
                version.getDateUpdate(),
                version.getVersionNum(),
                   shapes );
       return data;
    }
}
