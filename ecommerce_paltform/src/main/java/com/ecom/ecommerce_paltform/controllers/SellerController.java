package com.ecom.ecommerce_paltform.controllers;

import com.ecom.ecommerce_paltform.config.JwtProvider;
import com.ecom.ecommerce_paltform.helpers.AccountStatus;
import com.ecom.ecommerce_paltform.models.Seller;
import com.ecom.ecommerce_paltform.models.VerificationCode;
import com.ecom.ecommerce_paltform.repositories.VerificationCodeRepository;
import com.ecom.ecommerce_paltform.response.AuthResponse;
import com.ecom.ecommerce_paltform.response.LoginRequest;
import com.ecom.ecommerce_paltform.services.AuthService;
import com.ecom.ecommerce_paltform.services.EmailService;
import com.ecom.ecommerce_paltform.services.SellerService;
import com.ecom.ecommerce_paltform.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final AuthService authService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> sellerLoginHandler(
            @RequestBody LoginRequest req
            ) {

        String otp = req.getOtp();
        String email = req.getEmail();

        req.setEmail("seller_" + email);

        AuthResponse authResponse = authService.loginUser(req);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(
            @PathVariable String otp
    ) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){

            throw new Exception("wrong otp...");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {

        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();

        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "Appenston Email Verification Code " + otp;
        String text = "Welcome to Appenston, verify your accont using this link ";
        String frontendUrl = "http://localhost:3000/verify-seller/";

        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontendUrl);

        return new ResponseEntity<>(savedSeller, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception {

        Seller seller = sellerService.getSellerById(id);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(
            @RequestHeader("Authorization") String jwt) throws Exception {

        System.out.println(jwt);
        Seller seller = sellerService.getSellerProfile(jwt);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false)AccountStatus status){

        List<Seller> sellers = sellerService.getAllSellers(status);

        return ResponseEntity.ok(sellers);
    }

    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws Exception {

        Seller profile = sellerService.getSellerProfile(jwt);

        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);

        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {

        sellerService.deleteSeller(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{sellerId}")
    public ResponseEntity<Seller> updateAccountStatus(@PathVariable Long sellerId, @RequestParam(required = false)AccountStatus status) throws Exception {

        Seller seller = sellerService.updateSellerAccountStatus(sellerId, status);

        return ResponseEntity.ok(seller);
    }


}
