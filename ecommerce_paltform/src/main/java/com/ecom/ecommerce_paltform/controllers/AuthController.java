package com.ecom.ecommerce_paltform.controllers;


import com.ecom.ecommerce_paltform.helpers.USER_ROLE;
import com.ecom.ecommerce_paltform.models.VerificationCode;
import com.ecom.ecommerce_paltform.repositories.UserRepository;
import com.ecom.ecommerce_paltform.response.ApiResponse;
import com.ecom.ecommerce_paltform.response.AuthResponse;
import com.ecom.ecommerce_paltform.response.LoginRequest;
import com.ecom.ecommerce_paltform.response.SignupRequest;
import com.ecom.ecommerce_paltform.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> loginOtpHandler(@RequestBody VerificationCode req) throws Exception{


        authService.sentLoginOtp(req.getEmail());

        ApiResponse res = new ApiResponse();

        res.setMessage("Otp sent successfully");

        return ResponseEntity.ok(res);
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception{


        String jwt = authService.createUser(req);

        AuthResponse authResponse = new AuthResponse();

        authResponse.setJwt(jwt);
        authResponse.setMessage("User registered successfully");
        authResponse.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest req) throws Exception{
        
        AuthResponse authResponse = authService.loginUser(req);
        return ResponseEntity.ok(authResponse);
    }
}
