package com.company.service;

import com.company.model.User;
import com.company.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBService {

    @Autowired
    UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void insert(User user){
        userRepository.save(user);
    }

    public boolean login(String email, String password){
        if(userRepository.login(email,password) == 1){
            return true;
        }else {
            return false;
        }
    }
}
