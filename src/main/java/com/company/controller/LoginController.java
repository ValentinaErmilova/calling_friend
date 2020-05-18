package com.company.controller;

import com.company.dao.UserDAO;
import com.company.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    UserDAO userDAO;

    @GetMapping({"/","/history"})
    public String history(Model model){
        model.addAttribute("history");
        return "history";
    }

    @GetMapping("/result")
    public String result(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userDAO.findByEmail(username);
        model.addAttribute("result",user);
        return "result";
    }
}
