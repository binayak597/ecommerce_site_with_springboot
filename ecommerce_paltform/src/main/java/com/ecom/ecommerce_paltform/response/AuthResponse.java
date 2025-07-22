package com.ecom.ecommerce_paltform.response;

import com.ecom.ecommerce_paltform.helpers.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private String message;
    private USER_ROLE role;
}
