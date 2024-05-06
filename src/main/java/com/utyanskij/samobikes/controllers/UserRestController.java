package com.utyanskij.samobikes.controllers;

import com.utyanskij.samobikes.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


//Этот класс - контроллер REST API для проверки уникальности имени пользователя.
@RestController
public class UserRestController {
    private UserServiceImpl userService;

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }


    //Этот метод принимает два параметра - id и username.
    // Он вызывает метод isUsernameUnique из сервиса UserServiceImpl для проверки уникальности имени пользователя.
    // Если имя пользователя уникально для данного id,
    // метод возвращает строку "OK", в противном случае возвращает "Duplicated".
    @PostMapping("/check_username")
    public String checkUsername(@Param("id") Integer id, @Param("username") String username){
        if(userService.isUsernameUnique(id, username)) return "OK";
        else return "Duplicated";
    }
}
