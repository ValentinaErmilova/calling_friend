package com.company.service;

import com.company.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public boolean login(String email, String password){
        if(userRepository.login(email,password) == 1){
            return true;
        }else {
            return false;
        }
    }
}
