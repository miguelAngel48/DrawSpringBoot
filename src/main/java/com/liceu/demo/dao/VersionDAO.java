package com.liceu.demo.dao;

import com.liceu.demo.models.VersionDraw;

import java.util.List;

public interface VersionDAO {
    void saveVersion(VersionDraw vers);
    int findMaxVersionNumByDrawId(int drawId);
    VersionDraw getVersionForId(int id);
    public List<VersionDraw> getAllVersionForId(int drawid);
}
