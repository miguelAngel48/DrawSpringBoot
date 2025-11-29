package com.liceu.demo.dao;

import com.liceu.demo.models.VersionDraw;

public interface VersionDAO {
    void saveVersion(VersionDraw vers);
    int findMaxVersionNumByDrawId(int drawId);
}
