package com.company.controller;

import com.company.dao.CallDAO;
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

    @Autowired
    CallDAO callDAO;

    @GetMapping({"/","/history"})
    public String history(Model model){
        model.addAttribute("history");
        return "history";
    }

    @GetMapping("/result")
    public String result(Model model){
        model.addAttribute("result");
        return "result";
    }


}
