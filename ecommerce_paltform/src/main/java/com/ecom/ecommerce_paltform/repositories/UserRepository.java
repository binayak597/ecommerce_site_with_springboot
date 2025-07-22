package com.ecom.ecommerce_paltform.repositories;

import com.ecom.ecommerce_paltform.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
