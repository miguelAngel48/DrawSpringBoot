package com.liceu.demo.dao;

import com.liceu.demo.models.Shape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShapesDAOImplementsSQL implements ShapesDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;
    private RowMapper<Shape> shapeRowMapper = (rs, rn) -> {
        Shape s = new Shape();
        s.setId(rs.getInt("id"));
        s.setId(rs.getInt("idVersion"));
        s.setShapes(rs.getString("shapes"));
        return s;
    };

    @Override
    public void saveShapes(Shape shape) {
        jdbcTemplate.update("insert into shape(idVersion,shapes) values(?,?)",
                shape.getVersionId(),
                shape.getShapes());
    }

    @Override
    public Shape getShape(int idVersion) {
        List<Shape> s = jdbcTemplate.query("select * from shape where idVersion = ?", shapeRowMapper, idVersion);
        return s.get(0);
    }


}
