package com.example.pricecompareredis.service;

import com.example.pricecompareredis.vo.Keyword;
import com.example.pricecompareredis.vo.Product;
import com.example.pricecompareredis.vo.ProductGrp;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public int setNewProductGrpToKeyword(String keyword, String prodGrpId, double score) {
        myProdPriceRedis.opsForZSet().add(keyword, prodGrpId, score);
        int rank = myProdPriceRedis.opsForZSet().rank(keyword, prodGrpId).intValue();
        return rank;
    }

    @Override
    public Keyword getLowestPriceProductByKeyword(String keyword) {
        Keyword returnInfo = new Keyword();
        List<ProductGrp> tempProdGrp = new ArrayList<>();

        // keyword 를 통해 productGroup 가져오기
        tempProdGrp = getProductGrpUsingKeyword(keyword);

        // 가져온 정보들을 return 할 object 에 넣기
        returnInfo.setKeyword(keyword);
        returnInfo.setProductGrpList(tempProdGrp);

        // 해당 object 리턴
        return returnInfo;
    }

    public List<ProductGrp> getProductGrpUsingKeyword(String keyword) {

        List<ProductGrp> returnInfo = new ArrayList<>();

        // input 받은 keyword 로 productGrpId를 조회
        List<String> prodGrpIdList = new ArrayList<>();
        prodGrpIdList = List.copyOf(myProdPriceRedis.opsForZSet().reverseRange(keyword, 0, 9));
        List<Product> tempProdList = new ArrayList<>();

        // 10개 prodGrpId로 loop
        for (final String prodGrpId : prodGrpIdList) {
            // Loop 를 타면서 productGrpId 으로 product:price 가져오기 (10개)

            ProductGrp tempProGrp = new ProductGrp();

            Set ProdAndPriceList = new HashSet();
            ProdAndPriceList = myProdPriceRedis.opsForZSet().rangeWithScores(prodGrpId, 0, 9);

            // Loop 를 타면서 product object 에 바인em (10개)
            Iterator<Object> prodPriceObj = ProdAndPriceList.iterator();


            while(prodPriceObj.hasNext()) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> prodPriceMap = objectMapper.convertValue(prodPriceObj.next(), Map.class);

                // product object 에 바인드
                Product tempProduct = new Product();
                tempProduct.setProductId(prodPriceMap.get("value").toString()); // product id
                tempProduct.setPrice(Double.valueOf(prodPriceMap.get("score").toString()).intValue()); // elastic 검색된 score
                tempProduct.setProductGrpId(prodGrpId);

                tempProdList.add(tempProduct);
            }
            // 10개 product price 입력완료
            tempProGrp.setProductGrpId(prodGrpId);
            tempProGrp.setProductList(tempProdList);
            returnInfo.add(tempProGrp);
        }

        return returnInfo;
    }
}
