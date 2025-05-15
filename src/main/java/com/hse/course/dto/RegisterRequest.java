package com.hse.course.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный email")
    private String email;
    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;
    @NotBlank(message = "Имя пользователя обязательно")
    private String userName;


    public @NotBlank(message = "Email обязателен") @Email(message = "Некорректный email") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email обязателен") @Email(message = "Некорректный email") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Пароль обязателен") @Size(min = 6, message = "Пароль должен быть не менее 6 символов") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Пароль обязателен") @Size(min = 6, message = "Пароль должен быть не менее 6 символов") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Имя пользователя обязательно") String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank(message = "Имя пользователя обязательно") String userName) {
        this.userName = userName;
    }
}