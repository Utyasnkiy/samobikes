package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.DTO.UserDTO;
import com.utyanskij.samobikes.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;


//Этот интерфейс представляет сервисные методы, которые могут использоваться для управления пользователями,
// такими как поиск пользователя по имени пользователя и регистрация новых пользователей.
public interface UserService extends UserDetailsService {
    User findByUserName(String username);

    void registerNewUserAccount(UserDTO userDTO);
}
