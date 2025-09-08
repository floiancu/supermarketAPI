package com.supermarket.controller;

import com.supermarket.dto.CheckoutResponse;
import com.supermarket.service.SupermarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final SupermarketService supermarketService;

    @PostMapping("/checkout")
    ResponseEntity<CheckoutResponse> checkout(@RequestBody List<String> basketContents){
        return ResponseEntity.ok(new CheckoutResponse(this.supermarketService.checkout(basketContents)));
    }
}
