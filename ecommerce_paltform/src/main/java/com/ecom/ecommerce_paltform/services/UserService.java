package com.ecom.ecommerce_paltform.services;

import com.ecom.ecommerce_paltform.models.User;

public interface UserService {

    User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
