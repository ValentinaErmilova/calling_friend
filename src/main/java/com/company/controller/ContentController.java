package com.company.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Сontroller for receiving the part of the page that the user switches to on the page history.html.
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
