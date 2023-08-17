package com.example.pricecompareredis.service;

import com.example.pricecompareredis.vo.Product;
import com.example.pricecompareredis.vo.ProductGrp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LowestPriceServiceImpl implements LowestPriceService {

    private final RedisTemplate myProdPriceRedis;

    @Override
    public Set getZsetValue(String key) {
        Set myTempSet = new HashSet();
        myTempSet = myProdPriceRedis.opsForZSet().rangeByScoreWithScores(key, 0, 9);
        return myTempSet;
    }

    @Override
    public int setNewProduct(Product newProduct) {
        int rank;
        myProdPriceRedis.opsForZSet().add(newProduct.getProductGrpId(), newProduct.getProductId(), newProduct.getPrice());
        rank = myProdPriceRedis.opsForZSet().rank(newProduct.getProductGrpId(), newProduct.getProductId()).intValue();
        return rank;
    }

    @Override
    public int setNewProductGrp(ProductGrp newProductGrp) {
        List<Product> product = newProductGrp.getProductList();
        String productId = product.get(0).getProductId();
        int price = product.get(0).getPrice();
        myProdPriceRedis.opsForZSet().add(newProductGrp.getProductGrpId(), productId, price);
        int productCnt = myProdPriceRedis.opsForZSet().zCard(newProductGrp.getProductGrpId()).intValue();
        return productCnt;
    }

    @Override
    public int setNewProductGrpToKeyword(String keyword, String proGrpId, double score) {
        myProdPriceRedis.opsForZSet().add(keyword, proGrpId, score);
        int rank = myProdPriceRedis.opsForZSet().rank(keyword, proGrpId).intValue();
        return rank;
    }
}
