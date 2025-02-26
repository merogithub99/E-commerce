package com.sushant.controller;


import com.sushant.domain.USER_ROLE;
import com.sushant.model.User;
import com.sushant.response.AuthResponse;
import com.sushant.response.SignupRequest;
import com.sushant.service.AuthService;
import com.sushant.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
//    private  final AuthService authService;
//
    public UserController(UserService userService) {
        this.userService = userService;

    }




    @GetMapping("/users/profile")
    public ResponseEntity<User> userProfile(
            @RequestHeader("Authorization") String jwt
    )
            throws Exception {

        User user = userService.findUserByJwtToken(jwt);



        return ResponseEntity.ok(user);
    }

}
