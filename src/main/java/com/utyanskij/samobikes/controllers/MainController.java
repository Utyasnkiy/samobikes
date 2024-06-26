package com.utyanskij.samobikes.controllers;

import com.utyanskij.samobikes.DTO.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


//Этот код представляет собой контроллер главной страницы, а также страниц входа и регистрации
@Controller
public class MainController {
    @GetMapping("")
    public String showHomePage(){
        return "redirect:/bikes";
    }

    @GetMapping("/login")
    public String showLoginPage(){

        return "login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model){
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "registration";
    }


}
