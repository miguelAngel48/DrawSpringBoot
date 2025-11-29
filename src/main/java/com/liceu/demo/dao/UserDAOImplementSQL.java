package com.liceu.demo.dao;

import com.liceu.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImplementSQL implements UserDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = (rs, rn) -> {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setPassword(rs.getString("password"));
        u.setUsername(rs.getString("username"));
        return u;
    };

    @Override
    public User getUser(String username) {

        List<User> list = jdbcTemplate.query("select * from user where username=?", userRowMapper, username);
        return list.get(0);
    }

    @Override
    public int lastId() {
        return 0;
    }

    @Override
    public void save(User u) {
        jdbcTemplate.update("insert into user(name,password,username) values(?,?,?)", u.getName(), u.getPassword(), u.getUsername());
    }
    public boolean checkUsernameExists(String username) {

        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

}
