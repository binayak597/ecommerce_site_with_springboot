package com.ecom.ecommerce_paltform.repositories;

import com.ecom.ecommerce_paltform.models.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    VerificationCode findByEmail(String email);
}
