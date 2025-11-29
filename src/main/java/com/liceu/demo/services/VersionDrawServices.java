package com.liceu.demo.services;

import com.liceu.demo.dao.VersionDAO;
import com.liceu.demo.models.VersionDraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersionDrawServices {
@Autowired
    VersionDAO versionDAO;
public void saveVersionOnDataBase(int drawId){
    VersionDraw ver = new VersionDraw();
    ver.setDrawid(drawId);
    ver.setVersionNum(versionDAO.findMaxVersionNumByDrawId(drawId)+1);
    System.out.println(ver.getVersionNum());
    versionDAO.saveVersion(ver);
}
}
