package com.ecom.ecommerce_paltform.models;

import lombok.Data;

@Data
public class BankDetails {

    private String accountNumber;

    private String accountHolderName;

    private String ifscCode;
}
