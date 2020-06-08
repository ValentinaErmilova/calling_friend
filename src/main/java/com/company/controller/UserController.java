package com.company.controller;


import com.company.dao.UserDAO;
import com.company.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class UserController {
    @Autowired
    UserDAO userDAO;

    @GetMapping("/user/{phoneNumber}")
    public User getUser(@PathVariable String phoneNumber){
        return userDAO.findByPhonenumber(phoneNumber);
    }
}
