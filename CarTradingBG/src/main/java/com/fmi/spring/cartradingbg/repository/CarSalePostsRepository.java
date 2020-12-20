package com.fmi.spring.cartradingbg.repository;

import com.fmi.spring.cartradingbg.model.CarSalePost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarSalePostsRepository extends MongoRepository<CarSalePost,String> {

}
