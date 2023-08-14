package com.example.pricecompareredis.controller;

import com.example.pricecompareredis.service.LowestPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
