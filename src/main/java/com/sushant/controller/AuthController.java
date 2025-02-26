package com.sushant.controller;


import com.sushant.domain.USER_ROLE;
import com.sushant.model.User;
import com.sushant.model.VerificationCode;
import com.sushant.repository.UserRepository;
import com.sushant.request.LoginOtpRequest;
import com.sushant.request.LoginRequest;
import com.sushant.response.ApiResponse;
import com.sushant.response.AuthResponse;
import com.sushant.response.SignupRequest;
import com.sushant.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
//@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    public AuthController(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {
        //for no spring security
        //        User user = new User();
    //        user.setEmail(req.getEmail());
    //        user.setFullName(req.getFullName());
    //        User savedUser= userRepository.save(user);


        //after spring security
        String jwt = authService.createUser(req);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("register success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(
            @RequestBody LoginOtpRequest req) throws Exception {
        authService.sentLoginOtp(req.getEmail(),req.getRole());
        ApiResponse res = new ApiResponse();
        res.setMessage("otp sent successfully");
        return ResponseEntity.ok(res);
    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody LoginRequest req) throws Exception {
        AuthResponse authResponse = authService.signin(req);


        return ResponseEntity.ok(authResponse);
    }
}
