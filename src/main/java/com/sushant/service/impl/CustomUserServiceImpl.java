package com.sushant.service.impl;

import com.sushant.domain.USER_ROLE;
import com.sushant.model.Seller;
import com.sushant.model.User;
import com.sushant.repository.SellerRepo;
import com.sushant.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@RequiredArgsConstructor
@Service
public class CustomUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepo sellerRepo;

    private static final Logger logger = LoggerFactory.getLogger(CustomUserServiceImpl.class);

    private static final String SELLER_PREFIX = "seller_";

    public CustomUserServiceImpl(UserRepository userRepository, SellerRepo sellerRepo) {
        this.userRepository = userRepository;
        this.sellerRepo = sellerRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading user by username: {}", username);

        if (username.startsWith(SELLER_PREFIX)) {
            String actualUsername = username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepo.findByEmail(actualUsername);

            if (seller != null) {
                logger.debug("Loading user by username: {}", username);

                return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
            }

        } else {
            User user = userRepository.findByEmail(username);
            if (user != null) {
                logger.debug("User found: {}", user.getEmail());

                return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
            }
        }

        throw new UsernameNotFoundException("user or seller not found with email" + username);
    }

    private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
        if (role == null) role = USER_ROLE.ROLE_CUSTOMER;

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString()));

//        password = "dummy password";  if then buildUserDetails meth dont need pass parameter
        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                authorityList
        );

    }
}
