package com.hse.course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthResponse {
    private String token;
    private String message;
    private Long userId;
    private String refreshToken;
    private String email;
    private String userName;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private String token;
        private String message;
        private Long userId;
        private String refreshToken;
        private String email;
        private String userName;

        public AuthResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public AuthResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public AuthResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthResponseBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public AuthResponseBuilder refreshToken(String refreshToken){
            this.refreshToken = refreshToken;
            return  this;
        }

        public AuthResponse build() {
            AuthResponse response = new AuthResponse();
            response.setToken(this.token);
            response.setRefreshToken(this.refreshToken);
            response.setMessage(this.message);
            response.setUserId(this.userId);
            response.setEmail(this.email);
            response.setUserName(this.userName);
            return response;
        }
    }
}

//package com.hse.course.dto;  // или ваш пакет для DTO
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class AuthResponse {
//    private String token;          // JWT-токен
//    private String message;        // Опциональное сообщение (например, "Регистрация успешна")
//    private Long userId;          // ID пользователя (для удобства клиента)
//    private String email;         // Электронная почта пользователя
//    private String userName;      // Имя пользователя
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//}
