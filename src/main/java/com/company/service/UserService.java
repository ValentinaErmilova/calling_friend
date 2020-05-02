package com.company.service;

import com.company.model.User;
import com.company.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDAO userRepository;


    PasswordEncoder encoder = new BCryptPasswordEncoder();


    public boolean login(User user){
        String hash = userRepository.login(user.getEmail());

        if(encoder.matches(user.getPassword(),hash)){
            return true;
        }else {
            return false;
        }
    }

    public void save(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
