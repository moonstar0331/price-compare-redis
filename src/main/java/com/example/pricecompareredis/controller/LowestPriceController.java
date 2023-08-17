package com.example.pricecompareredis.controller;

import com.example.pricecompareredis.service.LowestPriceService;
import com.example.pricecompareredis.vo.Keyword;
import com.example.pricecompareredis.vo.Product;
import com.example.pricecompareredis.vo.ProductGrp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class LowestPriceController {

    private final LowestPriceService lowestPriceService;

    @GetMapping("/getZSETValue")
    public Set GetZsetValue(String key) {
        return lowestPriceService.getZsetValue(key);
    }

    @PutMapping("/product")
    public int setNewProduct(@RequestBody Product newProduct) {
        return lowestPriceService.setNewProduct(newProduct);
    }

    @PutMapping("/productGroup")
    public int setNewProduct(@RequestBody ProductGrp newProductGrp) {
        return lowestPriceService.setNewProductGrp(newProductGrp);
    }

    @PutMapping("/productGroupToKeyword")
    public int setNewProductGrpToKeyword(String keyword, String proGrpId, double score) {
        return lowestPriceService.setNewProductGrpToKeyword(keyword, proGrpId, score);
    }
}
