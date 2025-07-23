package com.ecom.ecommerce_paltform.impls;

import com.ecom.ecommerce_paltform.config.JwtProvider;
import com.ecom.ecommerce_paltform.helpers.AccountStatus;
import com.ecom.ecommerce_paltform.helpers.USER_ROLE;
import com.ecom.ecommerce_paltform.models.Address;
import com.ecom.ecommerce_paltform.models.Seller;
import com.ecom.ecommerce_paltform.repositories.AddressRepository;
import com.ecom.ecommerce_paltform.repositories.SellerRepository;
import com.ecom.ecommerce_paltform.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpls implements SellerService {

    private final JwtProvider jwtProvider;
    private final SellerRepository sellerRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Seller getSellerProfile(String jwt) throws Exception {

        String email = jwtProvider.getEmailFromJwtToken(jwt);

        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {

        Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());

        if(sellerExist != null){

            throw new Exception("seller alreay exist... use different email");
        }

        Address savedAddress = addressRepository.save(seller.getPickupAddress());

        Seller newSeller = new Seller();

        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);

    }

    @Override
    public Seller getSellerById(Long id) throws Exception {

        return sellerRepository.findById(id).orElseThrow(() -> new Exception("seller not found with id "+ id));

    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {

        Seller seller = sellerRepository.findByEmail(email);

        if(seller == null) {
            throw new Exception("seller not found..");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return sellerRepository.findAll();
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {

        Seller existingSeller = this.getSellerById(id);

        if(seller.getSellerName() != null){

            existingSeller.setSellerName(seller.getSellerName());
        }

        if(seller.getMobile() != null){

            existingSeller.setMobile(seller.getMobile());
        }

        if(seller.getEmail() != null){

            existingSeller.setEmail(seller.getEmail());
        }

        if(seller.getBusinessDetails() != null

        && seller.getBusinessDetails().getBusinessName() != null){

            existingSeller.getBusinessDetails().setBusinessName(
                    seller.getBusinessDetails().getBusinessName()
            );
        }

        if(seller.getBankDetails() != null

        && seller.getBankDetails().getAccountHolderName() != null
        && seller.getBankDetails().getIfscCode() != null
        && seller.getBankDetails().getAccountNumber() != null){

            existingSeller.getBankDetails().setAccountHolderName(
                    seller.getBankDetails().getAccountHolderName()
            );

            existingSeller.getBankDetails().setAccountNumber(
                    seller.getBankDetails().getAccountNumber()
            );

            existingSeller.getBankDetails().setIfscCode(
                    seller.getBankDetails().getIfscCode()
            );
        }

        if(seller.getPickupAddress() != null
        && seller.getPickupAddress().getAddress() != null
        && seller.getPickupAddress().getMobile() != null
        && seller.getPickupAddress().getCity() != null
        && seller.getPickupAddress().getState() != null){

            existingSeller.getPickupAddress().setAddress(
                    seller.getPickupAddress().getAddress()
            );

            existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            existingSeller.getPickupAddress().setPincode(seller.getPickupAddress().getPincode());

        }

        if(seller.getGSTIN() != null){
            existingSeller.setGSTIN(seller.getGSTIN());
        }

        return sellerRepository.save(existingSeller);


    }

    @Override
    public void deleteSeller(Long id) throws Exception {

        Seller seller = this.getSellerById(id);

        sellerRepository.delete(seller);


    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = this.getSellerByEmail(email);
        seller.setEmailVerified(true);

        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {

        Seller seller = this.getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}
