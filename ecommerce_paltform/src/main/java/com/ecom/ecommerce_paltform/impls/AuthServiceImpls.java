package com.ecom.ecommerce_paltform.impls;

import com.ecom.ecommerce_paltform.config.JwtProvider;
import com.ecom.ecommerce_paltform.helpers.USER_ROLE;
import com.ecom.ecommerce_paltform.models.Cart;
import com.ecom.ecommerce_paltform.models.Seller;
import com.ecom.ecommerce_paltform.models.User;
import com.ecom.ecommerce_paltform.models.VerificationCode;
import com.ecom.ecommerce_paltform.repositories.CartRepository;
import com.ecom.ecommerce_paltform.repositories.SellerRepository;
import com.ecom.ecommerce_paltform.repositories.UserRepository;
import com.ecom.ecommerce_paltform.repositories.VerificationCodeRepository;
import com.ecom.ecommerce_paltform.response.AuthResponse;
import com.ecom.ecommerce_paltform.response.LoginRequest;
import com.ecom.ecommerce_paltform.response.SignupRequest;
import com.ecom.ecommerce_paltform.services.AuthService;
import com.ecom.ecommerce_paltform.services.EmailService;
import com.ecom.ecommerce_paltform.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpls implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpls customUserServiceImpls;
    private final SellerRepository sellerRepository;

    @Override
    public void sentLoginOtp(String email, USER_ROLE role) throws Exception {

        String SIGNIN_PREFIX="signin_";

        if(email.startsWith(SIGNIN_PREFIX)){

            email = email.substring(SIGNIN_PREFIX.length());

            if(role.equals(USER_ROLE.ROLE_SELLER)){

                Seller seller = sellerRepository.findByEmail(email);

                if(seller == null){

                    throw new Exception("seller not found");
                }
            }else{

                User user = userRepository.findByEmail(email);

                if(user == null){

                    throw new Exception("user not exist with provided email");
                }
            }

        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);

        if(isExist != null){

            verificationCodeRepository.delete(isExist);
        }
        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();

        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);

        verificationCodeRepository.save(verificationCode);

        String subject = "Appenston Login / Signup";
        String text = "Your Login / Signup OTP is " + otp;

        emailService.sendVerificationOtpEmail(email, otp, subject, text);

    }

    @Override
    public String createUser(SignupRequest req) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());


        if(verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())){

            throw new Exception("wrong otp...");
        }

        User user = userRepository.findByEmail(req.getEmail());

        if(user == null){

            User createdUser = new User();

            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("9999999999");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

            user = userRepository.save(createdUser);

            Cart cart = new Cart();

            cart.setUser(user);

            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);

        return jwtProvider.generateToken(authentication);

    }

    @Override
    public AuthResponse loginUser(LoginRequest req) {

        String username = req.getEmail();

        String otp = req.getOtp();

        Authentication authentication = authenticate(username, otp);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login successfully");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String roleName = authorities.isEmpty()? null: authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {

        UserDetails userDetails =  customUserServiceImpls.loadUserByUsername(username);

        String SELLER_PREFIX="seller_";

        if(username.startsWith(SELLER_PREFIX)){

            username = username.substring(SELLER_PREFIX.length());
        }

        if(userDetails == null){

            throw new BadCredentialsException("invalid username");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){

            throw new BadCredentialsException("wrong otp");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
