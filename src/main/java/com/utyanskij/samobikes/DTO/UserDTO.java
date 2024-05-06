package com.utyanskij.samobikes.DTO;

public class UserDTO {
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    public UserDTO() {
    }

    public UserDTO(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

//Этот класс облегчает передачу информации о пользователе между контроллерами и службами веб-приложения,
// например, при регистрации новых пользователей или обновлении информации о существующих пользователях.
// Использование DTO помогает разделить слои приложения и упрощает обработку данных.