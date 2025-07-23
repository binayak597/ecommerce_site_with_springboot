package com.ecom.ecommerce_paltform.services;

import com.ecom.ecommerce_paltform.helpers.USER_ROLE;
import com.ecom.ecommerce_paltform.response.AuthResponse;
import com.ecom.ecommerce_paltform.response.LoginRequest;
import com.ecom.ecommerce_paltform.response.SignupRequest;
import jakarta.mail.MessagingException;

public interface AuthService {

    void sentLoginOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignupRequest req) throws Exception;
    AuthResponse loginUser(LoginRequest req);
}
