package com.liceu.demo.dao;

import com.liceu.demo.models.Draw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DrawDAOImplementSQL implements DrawDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<Draw> drawRowMapper = (rs,rn) ->{
        Draw d = new Draw();
        d.setId(rs.getInt("id"));
        d.setNameDraw(rs.getString("NameDraw"));
        d.setIdUser(rs.getInt("idUser"));
        d.setDateCreate(rs.getString("CreateDate"));
        d.setWidth(rs.getInt("width"));
        d.setHeight(rs.getInt("height"));
        d.setTrash(rs.getBoolean("trash"));
        return d;
    };
    @Override
    public void saveDraw( Draw draw) {
        int trashValue = draw.isTrash() ? 1 : 0;
        jdbcTemplate.update("insert into draw(NameDraw,idUser,width,height,trash) values(?,?,?,?,?)",
                 draw.getNameDraw()
                ,draw.getIdUser()
                ,draw.getWidth()
                ,draw.getHeight()
                ,trashValue);
    }

    @Override
    public List<Draw> getDrawsUser(String user) {

        return List.of();
    }

    @Override
    public void deleteDraw(String user, int idUser) {

    }

    @Override
    public Draw getDrawById(String user, int idDraw) {
        return null;
    }
}
