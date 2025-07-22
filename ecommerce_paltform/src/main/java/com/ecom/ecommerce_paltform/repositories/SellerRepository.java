package com.ecom.ecommerce_paltform.repositories;

import com.ecom.ecommerce_paltform.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Seller findByEmail(String name);
}
