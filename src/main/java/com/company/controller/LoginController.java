package com.company.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

   /* @Autowired
    private UserDAO userRepository;
    @Autowired
    private UserService userService;*/

    @GetMapping("/history")
    public String history(Model model){
        model.addAttribute("history");
        return "history";
    }

    @GetMapping("/result")
    public String result(Model model){
        model.addAttribute("result");
        return "result";
    }

    /*@PostMapping("/history")
    public String history(ModelMap model, @ModelAttribute User user){
        if(userService.login(user)){
            return "result";
        }else {
            model.addAttribute("history",user);
            return "history";
        }
    }*/
}
