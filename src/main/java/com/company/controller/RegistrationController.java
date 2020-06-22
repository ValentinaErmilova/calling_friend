package com.company.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Return registration page
@Controller
public class RegistrationController {

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

}
