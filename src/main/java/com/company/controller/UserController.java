package com.company.controller;


import com.company.dao.UserDAO;
import com.company.model.User;
import com.company.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class UserController {
    @Autowired
    UserDAO userDAO;

    @Autowired
    UserService userService;

    @GetMapping("/currentUser")
    public User currentUser(){
        return userService.getCurrentUser();
    }

    @GetMapping("/user")
    public User user(@RequestParam(name = "phoneNumber") String phoneNumber){
        return userDAO.findByPhonenumber(phoneNumber);
    }

    @GetMapping("/getFriends/{id}")
    public List<User> getFriends(@PathVariable int id){
        return userService.getFriends(id);
    }

    @DeleteMapping("/deleteFriend/{userId}/{friendId}")
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId){
        userDAO.deleteFriend(userId,friendId);
    }

    @PostMapping("/addFriend/{userId}")
    public User addFriend(@PathVariable int userId, @RequestParam(name = "phoneNumber") String phoneNumber) {
        return userService.addFriend(userId,phoneNumber);
    }
}
