package com.ecom.ecommerce_paltform.repositories;

import com.ecom.ecommerce_paltform.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
