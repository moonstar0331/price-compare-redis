package com.example.pricecompareredis.service;

import com.example.pricecompareredis.vo.Product;
import com.example.pricecompareredis.vo.ProductGrp;

import java.util.Set;

public interface LowestPriceService {
    Set getZsetValue(String key);

    int setNewProduct(Product newProduct);

    int setNewProductGrp(ProductGrp newProductGrp);

    int setNewProductGrpToKeyword(String keyword, String proGrpId, double score);
}
