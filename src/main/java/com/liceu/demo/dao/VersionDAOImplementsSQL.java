package com.liceu.demo.dao;

import com.liceu.demo.models.Draw;
import com.liceu.demo.models.VersionDraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VersionDAOImplementsSQL implements VersionDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<VersionDraw> versionDrawRowMapper =(rs, rn) ->{
        VersionDraw v = new VersionDraw();
        v.setVersionId(rs.getInt("versionId"));
        v.setDrawid(rs.getInt("drawid"));
        v.setDateUpdate(rs.getString("dateUpdate"));
        v.setVersionNum(rs.getInt("versionNum"));
        return v;
    };

    @Override
    public void saveVersion(VersionDraw vers) {
        jdbcTemplate.update("insert into version(drawid,versionNum) values(?,?)",vers.getDrawid(),vers.getVersionNum());
    }
    public int findMaxVersionNumByDrawId(int drawId) {
        String sql = "SELECT MAX(versionNum) FROM version WHERE drawId = ?";
        Integer maxVersion = jdbcTemplate.queryForObject(sql, Integer.class, drawId);
        return (maxVersion == null) ? 0 : maxVersion;
    }
    public VersionDraw getVersionForId(int drawid){
        String sql = "SELECT * From version WHERE drawid = ?";
        List<VersionDraw> version =  jdbcTemplate.query(sql,versionDrawRowMapper,drawid);
        return version.get(0);
    }
}
