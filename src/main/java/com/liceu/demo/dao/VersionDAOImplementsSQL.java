package com.liceu.demo.dao;

import com.liceu.demo.models.VersionDraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
}
