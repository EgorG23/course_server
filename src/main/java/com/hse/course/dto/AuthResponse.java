package com.hse.course.dto;

import lombok.*;

@Setter
@Getter
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String message;
    private Long globalId;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private Long dob;

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private String token;
        private String refreshToken;
        private String message;
        private Long globalId;
        private String email;
        private String password;
        private String name;
        private String surname;
        private String phoneNumber;
        private Long dob;

        public AuthResponseBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public AuthResponseBuilder refreshToken(String refreshToken){
            this.refreshToken = refreshToken;
            return  this;
        }

        public AuthResponseBuilder globalId(Long globalId) {
            this.globalId = globalId;
            return this;
        }

        public AuthResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthResponseBuilder password(String password){
            this.password = password;
            return this;
        }

        public AuthResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AuthResponseBuilder surname(String surname){
            this.surname = surname;
            return this;
        }

        public AuthResponseBuilder phoneNumber(String phoneNumber){
            this.phoneNumber = phoneNumber;
            return this;
        }

        public AuthResponseBuilder dob(Long dob){
            this.dob = dob;
            return this;
        }



        public AuthResponse build() {
            AuthResponse response = new AuthResponse();
            response.setToken(this.token);
            response.setRefreshToken(this.refreshToken);
            response.setMessage(this.message);
            response.setGlobalId(this.globalId);
            response.setEmail(this.email);
            response.setPassword(this.password);
            response.setName(this.name);
            response.setSurname(this.surname);
            response.setPhoneNumber(this.phoneNumber);
            response.setDob(this.dob);
            return response;
        }
    }
}
