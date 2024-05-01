package com.utyanskij.samobikes.services;

import com.utyanskij.samobikes.repositories.UserRepository;
import com.utyanskij.samobikes.entities.SamUserDetails;
import com.utyanskij.samobikes.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SamUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(username);
        if (user != null){
            return new SamUserDetails(user);
        }
        throw new UsernameNotFoundException("Не найден пользователь с именем " + username);
    }
}
