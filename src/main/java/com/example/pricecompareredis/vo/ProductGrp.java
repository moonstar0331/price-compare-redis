package com.example.pricecompareredis.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductGrp {
    private String productGrpId; // FPG0001
    private List<Product> productList; //[{d1fc1031-da1c-40da-9cd1-e9fef3f2a336, 25000}, {} ...]
}
