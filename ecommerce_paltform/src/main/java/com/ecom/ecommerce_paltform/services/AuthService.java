package com.ecom.ecommerce_paltform.services;

import com.ecom.ecommerce_paltform.response.AuthResponse;
import com.ecom.ecommerce_paltform.response.LoginRequest;
import com.ecom.ecommerce_paltform.response.SignupRequest;
import jakarta.mail.MessagingException;

public interface AuthService {

    void sentLoginOtp(String email) throws Exception;
    String createUser(SignupRequest req) throws Exception;
    AuthResponse loginUser(LoginRequest req);
}
