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
        return loginUser != null && encrypterPassword.CheckEncriptPassword(password, loginUser.getPassword());
    }

    public User getUser(String username){
        return  userDAO.getUser(username);
    }

    public boolean saveUser(String name, String password,String username,String passwordCheck){
        if (isValidRegistrationData(name,password,username,passwordCheck)){
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

    public boolean isValidRegistrationData(String name, String password, String username, String passwordCheck) {

        if (!userIsValid(name, password, username, passwordCheck)) {
            return false;
        }
        return !userDAO.checkUsernameExists(username);
    }

    private boolean userIsValid(String name, String password, String username, String passwordCheck) {
      return  (password.equals(passwordCheck) && password.length() > 5 && !username.isEmpty() && !name.isEmpty());


    }


}

