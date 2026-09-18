package com.github.noeeekr.servicerized.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/product") 
public class ProductController {
    
    @PostMapping()
    public void createProduct() {
        
    }
}
