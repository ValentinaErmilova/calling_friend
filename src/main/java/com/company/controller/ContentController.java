package com.company.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @GetMapping("/calls")
    public String calls(){
        return "calls";
    }

    @GetMapping("/friends")
    public String friends(){
        return "friends";
    }
}
