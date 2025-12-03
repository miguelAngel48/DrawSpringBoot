package com.liceu.demo.dao;

import com.liceu.demo.models.Draw;
import com.liceu.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class DrawDAOImplementSQL implements DrawDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<Draw> drawRowMapper = (rs, rn) -> {
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
    public int saveDraw(Draw draw) {
        if (draw.getId() > 0) {
            updateDraw(draw);
            return draw.getId();
        } else {
            int trashValue = draw.isTrash() ? 1 : 0;
            String sql = "INSERT INTO draw(NameDraw,idUser,width,height,trash,publico) VALUES(?,?,?,?,?,?)";
//            jdbcTemplate.update("INSERT INTO draw(NameDraw,idUser,width,height,trash) VALUES(?,?,?,?,?)",
//                    draw.getNameDraw(), draw.getIdUser(), draw.getWidth(), draw.getHeight(), trashValue);

//            Integer lastId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
//            return lastId != null ? lastId : 0;
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, draw.getNameDraw());
                ps.setInt(2, draw.getIdUser());
                ps.setInt(3, draw.getWidth());
                ps.setInt(4, draw.getHeight());
                ps.setBoolean(5, draw.isTrash());
                ps.setBoolean(6,draw.isPublico());
                return ps;
            }, keyHolder);

            // Obtener el ID autogenerado
            Number id = keyHolder.getKey();

            return id.intValue();
        }
    }

    @Override
    public List<Draw> getDrawsUser(int userId) {
        return jdbcTemplate.query("select * from draw where idUser = ?", drawRowMapper,userId);
    }
    public List<Draw> getDrawsPublics(){
        return  jdbcTemplate.query("select * from draw where publico = 1", drawRowMapper);
    }


    @Override
    public void deleteDraw(String user, int idUser) {

    }

    public void updateDraw(Draw draw) {
        int trashValue = draw.isTrash() ? 1 : 0;
        jdbcTemplate.update("UPDATE draw SET NameDraw = ?, idUser = ?, width = ?, height = ?, trash = ? WHERE id = ?",
                draw.getNameDraw()
                , draw.getIdUser()
                , draw.getWidth()
                , draw.getHeight()
                , trashValue
                , draw.getId()
        );


    }

    @Override
    public int getDrawById() {
        try {
            String sql = "SELECT id FROM draw ORDER BY id DESC LIMIT 1";
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
            return result.intValue();
        } catch (Exception e) {
            return -1;
        }
    }

    public Draw getDrawForId(int id) {
        String sql = "SELECT * From draw WHERE id = ?";
        Draw d = new Draw();
        List<Draw> draws = jdbcTemplate.query(sql, drawRowMapper, id);
        return draws.get(0);
    }
}
