package com.sushant.service;

import com.sushant.domain.USER_ROLE;
import com.sushant.request.LoginRequest;
import com.sushant.response.AuthResponse;
import com.sushant.response.SignupRequest;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {
//auth service
    String createUser(SignupRequest req) throws Exception;

    void sentLoginOtp(String email, USER_ROLE role) throws Exception;

    AuthResponse signin(LoginRequest req) throws Exception;

}
