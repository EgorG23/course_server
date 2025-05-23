package com.hse.course.controller;

import com.hse.course.dto.RegisterRequest;
import com.hse.course.model.User;
import com.hse.course.service.ApiResponse;
import com.hse.course.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/get/email/{email}")
    public ApiResponse getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/user/get/id/{id}")
    public ApiResponse getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping("/user/update/")
    public ApiResponse updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/user/interests/{userId}")
    public ApiResponse getUserInterests(@PathVariable Long userId) {
        return userService.getUserInterests(userId);
    }

    @PostMapping("/user/interests/{userId}")
    public ApiResponse updateInterests(
            @PathVariable Long userId,
            @RequestBody String interests
    ) {
        return userService.updateInterests(userId, interests);
    }

    @GetMapping("user/get/loyalty/{userGlobalId}") // Для получения card
    public ApiResponse getLoyaltyCard(@PathVariable String userGlobalId) {
        return userService.getLoyaltyCard(userGlobalId);
    }
}