package com.ecom.ecommerce_paltform.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {

    private String error;
    private String details;
    private LocalDateTime timestamp;

}
