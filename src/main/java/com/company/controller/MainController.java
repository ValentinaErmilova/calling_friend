package com.company.controller;

import com.company.model.User;
import com.company.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;


@Controller
public class MainController {

    @Autowired
    private DBService dbService;

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("regForm",new User());
        return "login/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user){
        dbService.insert(user);
        return "login/result";
    }


    @GetMapping("/mainForm")
    public String mainForm(Model model){
        model.addAttribute("mainForm",new User());
        return "mainForm";
    }

    @PostMapping("/mainForm")
    public String mainForm(ModelMap model, @ModelAttribute User user){

        if(dbService.login(user.getEmail(),user.getPassword())){
            return "login/result";
        }else {
            model.addAttribute("mainForm",user);
            return "mainForm";
        }
    }


}
