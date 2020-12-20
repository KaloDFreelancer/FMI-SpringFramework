package com.fmi.spring.cartradingbg.service;

import com.fmi.spring.cartradingbg.model.CarSalePost;

import java.util.List;
import java.util.Set;

public interface CarSalePostsService {
    List<CarSalePost> getAllCarSalePosts();
    CarSalePost getCarSalePostById(String id);
    CarSalePost addCarSalePost(CarSalePost CarSalePost);
    CarSalePost updateCarSalePost(CarSalePost CarSalePost);
    CarSalePost deleteCarSalePost(String id);
    long getCount();
}
