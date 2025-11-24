package com.liceu.demo.services;

import com.liceu.demo.dao.UserDAO;
import com.liceu.demo.models.User;
import com.liceu.demo.security.EncrypterPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
    @Autowired
    UserDAO userDAO;
    @Autowired
    EncrypterPassword encrypterPassword;

    public boolean checkLogin(String username, String password){
        User loginUser = getUser(username);
        if (loginUser != null && encrypterPassword.CheckEncriptPassword(password,loginUser.getPassword())){
            return true;
        }
        return false;
    }

    public User getUser(String username){
        return  userDAO.getUser(username);
    }

    public boolean saveUser(String name, String password,String username,String passwordCheck){
        if (password.equals(passwordCheck) && password.length() > 5 && !username.isEmpty() && !name.isEmpty()){
            password = encrypterPassword.EncriptPassword(password);
            User u = new User();
            u.setName(name);
            u.setPassword(password);
            u.setUsername(username);
            userDAO.save(u);
            return true;
        }
        return false;
    }


}

