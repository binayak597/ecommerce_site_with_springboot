package com.ecom.ecommerce_paltform.response;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String otp;
}
