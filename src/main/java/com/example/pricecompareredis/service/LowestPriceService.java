package com.example.pricecompareredis.service;

import com.example.pricecompareredis.vo.Keyword;
import com.example.pricecompareredis.vo.Product;
import com.example.pricecompareredis.vo.ProductGrp;

import java.util.Set;

public interface LowestPriceService {
    Set getZsetValue(String key);

    Set getZSetValueWithStatus(String key) throws Exception;

    Set getZSetValueWithSpecificException(String key) throws Exception;

    int setNewProduct(Product newProduct);

    int setNewProductGrp(ProductGrp newProductGrp);

    int setNewProductGrpToKeyword(String keyword, String prodGrpId, double score);

    Keyword getLowestPriceProductByKeyword(String keyword);
}
