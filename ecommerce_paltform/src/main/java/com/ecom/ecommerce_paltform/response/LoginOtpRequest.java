package com.ecom.ecommerce_paltform.response;

import com.ecom.ecommerce_paltform.helpers.USER_ROLE;
import lombok.Data;

@Data
public class LoginOtpRequest {

    private String email;
    private String otp;
    private USER_ROLE role;
}
