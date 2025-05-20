package com.hse.course.dto;

import lombok.*;

@Setter
@Getter
public class AuthResponse {
    private String token;
    private String message;
    private Long userId;
    private String refreshToken;
    private String email;
    private String userName;

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
