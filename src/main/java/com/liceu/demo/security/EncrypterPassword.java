package com.liceu.demo.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class EncrypterPassword {
    private static final int COST_ENCRIPT = 12;

    public String EncriptPassword(String password){
        String jumps = BCrypt.gensalt(COST_ENCRIPT);
        return BCrypt.hashpw(password,jumps);
    }

    public boolean CheckEncriptPassword(String checkPassword, String storePassword){
        return BCrypt.checkpw(checkPassword,storePassword);
    }
}
