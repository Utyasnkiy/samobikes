package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.DTO.UserDTO;
import com.utyanskij.samobikes.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUserName(String username);

    void registerNewUserAccount(UserDTO userDTO);
}
