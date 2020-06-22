package com.company.service;

import com.company.exception.UserNotFoundException;
import com.company.model.User;
import com.company.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    PasswordEncoder encoder;

    public void save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    public List<User> getFriends(int id) {
        return userDAO.getFriends(id);
    }

    public User addFriend(int userID, String phoneNumber) {

        User friend = userDAO.findByPhonenumber(phoneNumber);
        if (friend == null) {
            throw new UserNotFoundException();
        }

        userDAO.addFriend(userID, friend.getId());

        return friend;

    }

    public User getCurrentUser() {
        String username;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userDAO.findByEmail(username);
    }
}
