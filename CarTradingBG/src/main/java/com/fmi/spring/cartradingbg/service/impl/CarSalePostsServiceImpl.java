package com.fmi.spring.cartradingbg.service.impl;

import com.fmi.spring.cartradingbg.exception.NoExistingEntityException;
import com.fmi.spring.cartradingbg.model.CarSalePost;
import com.fmi.spring.cartradingbg.repository.CarSalePostsRepository;
import com.fmi.spring.cartradingbg.service.CarSalePostsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarSalePostsServiceImpl implements CarSalePostsService {
    private final CarSalePostsRepository carSalePostsRepository;

    public CarSalePostsServiceImpl(CarSalePostsRepository carSalePostsRepository) {
        this.carSalePostsRepository = carSalePostsRepository;
    }

    @Override
    public List<CarSalePost> getAllCarSalePosts() {
        return carSalePostsRepository.findAll();
    }

//    @Override
//    public List<CarSalePost> getAllCarSalePostsByKeywords(Set<String> keywords) {
//        return carSalePostsRepository.findAllByKeywordsContaining(keywords);
//    }

    @Override
    public CarSalePost getCarSalePostById(String id) {
        return carSalePostsRepository.findById(id).orElseThrow(() ->
                new NoExistingEntityException(String.format("CarSalePost with ID:%s does not exist.", id)));
    }

    @Override
    public CarSalePost addCarSalePost(CarSalePost carSalePost) {
        carSalePost.setId(null);
        return carSalePostsRepository.insert(carSalePost);
    }

    @Override
    public CarSalePost updateCarSalePost(CarSalePost carSalePost) {
        getCarSalePostById(carSalePost.getId()); //also checks if id exists
        return carSalePostsRepository.save(carSalePost);
    }

    @Override
    public CarSalePost deleteCarSalePost(String id) {
        CarSalePost removed = getCarSalePostById(id);
        carSalePostsRepository.deleteById(id);
        return removed;
    }

    @Override
    public long getCount() {
        return carSalePostsRepository.count();
    }


}
