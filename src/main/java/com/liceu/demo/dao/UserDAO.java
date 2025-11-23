package com.liceu.demo.dao;

import com.liceu.demo.models.User;

public interface UserDAO {
    User getUser(String username);
    int lastId();
    void save(User u);
}
