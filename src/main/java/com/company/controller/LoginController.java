package com.company.controller;

import com.company.model.User;
import com.company.dao.UserDAO;
import com.company.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserDAO userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/historyCall")
    public String mainForm(Model model){
        model.addAttribute("historyCall",new User());
        return "historyCall";
    }

    @PostMapping("/historyCall")
    public String mainForm(ModelMap model, @ModelAttribute User user){
        if(userService.login(user)){
            return "result";
        }else {
            model.addAttribute("historyCall",user);
            return "historyCall";
        }
    }
}
