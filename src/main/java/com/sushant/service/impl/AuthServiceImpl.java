package com.sushant.service.impl;

import com.sushant.config.JwtProvider;
import com.sushant.domain.USER_ROLE;
import com.sushant.model.Cart;
import com.sushant.model.Seller;
import com.sushant.model.User;
import com.sushant.model.VerificationCode;
import com.sushant.repository.CartRepo;
import com.sushant.repository.SellerRepo;
import com.sushant.repository.UserRepository;
import com.sushant.repository.VerificationCodeRepo;
import com.sushant.request.LoginRequest;
import com.sushant.response.AuthResponse;
import com.sushant.response.SignupRequest;
import com.sushant.service.AuthService;
import com.sushant.service.EmailService;
import com.sushant.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
//@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeRepo verificationCodeRepo;

    private final CartRepo cartRepo;

    private final JwtProvider jwtProvider;

    private final CustomUserServiceImpl customUserService;


    private final EmailService emailService;
    private  final SellerRepo sellerRepo;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder, VerificationCodeRepo verificationCodeRepo,
                           CartRepo cartRepo,
                           JwtProvider jwtProvider, CustomUserServiceImpl customUserService, EmailService emailService, SellerRepo sellerRepo) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationCodeRepo = verificationCodeRepo;
        this.cartRepo = cartRepo;
        this.jwtProvider = jwtProvider;
        this.customUserService = customUserService;
        this.emailService = emailService;
        this.sellerRepo = sellerRepo;
    }

    @Override
    public String createUser(SignupRequest req) throws Exception {

        VerificationCode verificationCode = verificationCodeRepo.findByEmail(req.getEmail());

        if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new Exception("Wrong otp");
        }

        User user = userRepository.findByEmail(req.getEmail());
        if (user == null) {
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("545644646");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

            user = userRepository.save(createdUser);
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepo.save(cart);

        }

        //to generate jwt token
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }



    // 4 49 sentlogin otp
    @Override
    public void sentLoginOtp(String email,USER_ROLE role ) throws Exception {
        String SIGNING_PREFIX = "signing_";

        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());

            if(role.equals(USER_ROLE.ROLE_SELLER)){
                Seller seller = sellerRepo.findByEmail(email);
                if (seller==null){
                    throw  new Exception("seller not found");
                }


            }
            else{
                User user = userRepository.findByEmail(email);
                if (user == null) {
                    throw new Exception("user not exist with provided email");
                }
            }




        }

        VerificationCode isExist = verificationCodeRepo.findByEmail(email);
        if (isExist != null) {
            verificationCodeRepo.delete(isExist);
        }

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepo.save(verificationCode);

        String subject = "sushant bazaar loign/signup otp";
        String text = "your login/signup otp is - " + otp;

        emailService.sendVerificationOtpEmail(email, otp, subject, text);


    }

    @Override
    public AuthResponse signin(LoginRequest req) throws Exception {
        String username = req.getEmail();
        String otp = req.getOtp();

        Authentication authentication = authenticate(username, otp);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login success");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(roleName));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) throws Exception {
      UserDetails userDetails= customUserService.loadUserByUsername(username);

      String SELLER_PREFIX = "seller_";
      if(username.startsWith(SELLER_PREFIX)){
          username=username.substring(SELLER_PREFIX.length());
      }
      if (userDetails==null){
          throw  new BadCredentialsException("invalid username or passsword");
      }

      VerificationCode verificationCode =verificationCodeRepo.findByEmail(username);

      if(verificationCode==null || !verificationCode.getOtp().equals(otp)){
          throw new Exception("wrong otp");
      }
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
    }
}
