package com.divyanshu.productservice.controller;


import com.divyanshu.productservice.dto.Coupon;
import com.divyanshu.productservice.model.Product;
import com.divyanshu.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@RestController
@RequestMapping("/productapi")
public class ProductRestController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RestTemplate restTemplate;

    @Value("${couponService.url}")
    private String couponServiceUrl;

//    @PostMapping("/products")
//    public Product createProduct(@RequestBody Product product, @RequestHeader("Authorization") String authorizationHeader) {
//        String encodedCredentials = authorizationHeader.replace("Basic ", "");
//        byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
//        String decodedCredentials = new String(decodedBytes);
//        String[] credentials = decodedCredentials.split(":");
//
//        String username = credentials[0];
//        String password = credentials[1];
//
//        // Forward the credentials to the coupon API
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBasicAuth(username, password); // Set Basic Auth header
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<Coupon> response = restTemplate.exchange(
//                couponServiceUrl+product.getCouponCode(),
//                HttpMethod.GET,
//                entity,
//                Coupon.class
//        );
//
//        // Update product details with coupon information
//        product.setPrice(product.getPrice().subtract(response.getBody().getDiscount()));
//        return productRepository.save(product);
//    }

//    @PostMapping("/products")
//    public Product createProduct(@RequestBody Product product, @RequestHeader("Authorization") String authorizationHeader) {
//        Coupon coupon = restTemplate.getForObject(couponServiceUrl + product.getCouponCode(), Coupon.class);
//        product.setPrice(product.getPrice().subtract(coupon.getDiscount()));
//        return productRepository.save(product);
//    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product, @RequestHeader("Authorization") String authorizationHeader) {
        String encodedCredentials = authorizationHeader.replace("Basic ", "");
        byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
        String decodedCredentials = new String(decodedBytes);
        String[] credentials = decodedCredentials.split(":");
        String username = credentials[0];
        String password = credentials[1];

        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(username, password));

        Coupon coupon = restTemplate.getForObject(couponServiceUrl + product.getCouponCode(), Coupon.class);
        product.setPrice(product.getPrice().subtract(coupon.getDiscount()));
        return productRepository.save(product);
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        return productRepository.findById(id).orElse(null);
    }

}
