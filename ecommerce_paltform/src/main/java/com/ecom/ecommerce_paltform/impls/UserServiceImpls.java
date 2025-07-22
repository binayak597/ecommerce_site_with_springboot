package com.ecom.ecommerce_paltform.impls;

import com.ecom.ecommerce_paltform.config.JwtProvider;
import com.ecom.ecommerce_paltform.models.User;
import com.ecom.ecommerce_paltform.repositories.UserRepository;
import com.ecom.ecommerce_paltform.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpls implements UserService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {

        String email = jwtProvider.getEmailFromJwtToken(jwt);

        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new Exception("user not found with this email "+ email);

        }

        return user;
    }
}
