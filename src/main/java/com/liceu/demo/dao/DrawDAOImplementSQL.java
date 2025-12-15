package com.liceu.demo.dao;

import com.liceu.demo.models.Draw;
import com.liceu.demo.models.Share;
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
            String sql = "INSERT INTO draw(NameDraw,idUser,width,height,trash,publico) VALUES(?,?,?,?,?,?)";
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
                ps.setBoolean(6, draw.isPublico());
                return ps;
            }, keyHolder);

            // Obtener el ID autogenerado
            Number id = keyHolder.getKey();

            return id.intValue();
        }
    }

    @Override
    public List<Draw> getDrawsUser(int userId) {
        return jdbcTemplate.query("select * from draw where idUser = ? and trash = 0", drawRowMapper, userId);
    }

    @Override
    public List<Draw> getDrawsDeletedUser(int userId) {
        return jdbcTemplate.query("select * from draw where idUser = ? and trash = 1", drawRowMapper, userId);
    }

    public List<Draw> getDrawsPublics() {
        return jdbcTemplate.query("select * from draw where publico = 1 and trash = 0", drawRowMapper);
    }


    @Override
    public void deleteDraw(int idDraw) {
    jdbcTemplate.update("DELETE FROM draw WHERE id = ?",idDraw);
    }

    @Override
    public void updateDraw(Draw draw) {
        int trashValue = draw.isTrash() ? 1 : 0;
        jdbcTemplate.update("UPDATE draw SET NameDraw = ?, idUser = ?, width = ?, height = ?, trash = ?,publico = ? WHERE id = ?",
                draw.getNameDraw()
                , draw.getIdUser()
                , draw.getWidth()
                , draw.getHeight()
                , trashValue
                , draw.isPublico()
                , draw.getId()
        );

    }

    @Override
    public void updateStatTrash(int idDraw, boolean trash) {
        jdbcTemplate.update("UPDATE draw SET trash = ? WHERE id = ?", trash, idDraw);
    }
    public void updateStatPublic(int idDraw, boolean publico){
        jdbcTemplate.update("UPDATE draw SET publico = ? WHERE id = ?", publico, idDraw);
    }

    @Override
    public Draw getDrawById(int idDraw) {
          return jdbcTemplate.queryForObject("SELECT * FROM draw WHERE id = ?", drawRowMapper,idDraw);
    }

    @Override
    public Draw getDrawForId(int id) {
        String sql = "SELECT * FROM draw WHERE id = ?";
        List<Draw> draws = jdbcTemplate.query(sql, drawRowMapper, id);
        return draws.isEmpty() ? null : draws.get(0);
    }
    @Override
    public void saveShare(int idDraw, int idUser, Share.SharePermission permission) {
        jdbcTemplate.update(
                "INSERT INTO share(drawId, userId, permiso) VALUES (?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE permiso = VALUES(permiso)",
                idDraw, idUser, permission.name()
        );
    }
    @Override
    public List<Share> findSharedByUser(int ownerId) {
        return jdbcTemplate.query(
                "SELECT * FROM share s JOIN draw d ON s.drawId = d.id WHERE d.userId = ?",
                (rs, row) -> {
                    Share s = new Share();
                    s.setDrawId(rs.getInt("drawId"));
                    s.setUserId(rs.getInt("userId"));
                    s.setPermission(Share.SharePermission.valueOf(rs.getString("permiso")));
                    return s;
                },
                ownerId
        );
    }


    public List<Share> findSharedWithUser(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM share WHERE userId = ?",
                (rs, row) -> {
                    Share s = new Share();
                    s.setDrawId(rs.getInt("drawId"));
                    s.setUserId(rs.getInt("userId"));
                    s.setPermission(Share.SharePermission.valueOf(rs.getString("permiso")));
                    return s;
                },
                userId
        );
    }
    @Override
    public boolean isDrawSharedWithUser(int drawId, int userId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM share WHERE drawId = ? AND userId = ?",
                Integer.class,
                drawId,
                userId
        );
        return count != null && count > 0;
    }
    public Share.SharePermission getPermission(int drawId, int userId) {
        List<Share.SharePermission> permissions =
                jdbcTemplate.query(
                        "SELECT permiso FROM share WHERE drawId = ? AND userId = ?",
                        (rs, row) -> Share.SharePermission.valueOf(rs.getString("permiso")),
                        drawId,
                        userId
                );

        return permissions.isEmpty() ? null : permissions.get(0);
    }

}
