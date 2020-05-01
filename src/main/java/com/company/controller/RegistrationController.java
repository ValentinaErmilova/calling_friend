package com.company.controller;

import com.company.model.User;
import com.company.repository.UserRepository;
import com.company.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService dbService;

    @GetMapping("/registration")
    public String registration(Model model){
        System.out.println("!1");
        model.addAttribute("registration",new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user){
        System.out.println("!2");
        userRepository.save(user);
        return "result";
    }

}
