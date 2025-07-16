package com.ecom.ecommerce_paltform.controllers;

import com.ecom.ecommerce_paltform.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ApiResponse greet(){

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("heyy there! is everything okkk...");
        return apiResponse;
    }
}
