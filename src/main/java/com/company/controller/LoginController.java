package com.company.controller;

import com.company.dao.CallDAO;
import com.company.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// Controller for receiving login(login.html) page or main page (history.html)
@Controller
public class LoginController {

    @Autowired
    UserDAO userDAO;

    @Autowired
    CallDAO callDAO;

    @GetMapping({"/","/login"})
    public String login(Model model){
        model.addAttribute("login");
        return "login";
    }

    @GetMapping("/history")
    public String history(Model model){
        model.addAttribute("history");
        return "history";
    }
}
