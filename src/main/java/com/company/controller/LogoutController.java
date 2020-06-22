package com.company.controller;

import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Exit the application after pressing the corresponding button
@RestController
@RequestMapping("/rest")
public class LogoutController {

    @PostMapping("/logout")
    public void logout(HttpServletRequest rq, HttpServletResponse rs){
        SecurityContextLogoutHandler securityContextLogoutHandler =
                new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(rq, rs, null);
    }
}
