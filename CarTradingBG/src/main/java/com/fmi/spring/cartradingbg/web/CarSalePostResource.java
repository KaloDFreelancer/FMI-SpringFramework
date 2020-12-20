package com.fmi.spring.cartradingbg.web;

import com.fmi.spring.cartradingbg.exception.InvalidEntityDataException;
import com.fmi.spring.cartradingbg.model.CarSalePost;
import com.fmi.spring.cartradingbg.service.CarSalePostsService;
import com.fmi.spring.cartradingbg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.fmi.spring.cartradingbg.util.ErrorHandlingUtils.getViolationsAsStringList;

@RestController
@RequestMapping("/api/car-sale-posts")
public class CarSalePostResource {
    private final CarSalePostsService salePostsService;
    private final UserService userService;

    public CarSalePostResource(CarSalePostsService salePostsService, UserService userService) {
        this.salePostsService = salePostsService;
        this.userService = userService;
    }

    @GetMapping
    public List<CarSalePost> getAllCarSalePosts() {
        return salePostsService.getAllCarSalePosts();
    }

    @GetMapping("/{id}")
    public CarSalePost getCarSalePostById(@PathVariable("id") String id) {
        return salePostsService.getCarSalePostById(id);
    }

    @PostMapping
    public ResponseEntity<CarSalePost> createCarSalePost(Principal principal, @Valid @RequestBody CarSalePost carSalePost, Errors errors) {
        String principalId = userService.findByUsername(principal.getName()).get().getId();
        carSalePost.setSellerId(principalId);
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid Car Sale Post data", getViolationsAsStringList(errors));
        }
        CarSalePost created = salePostsService.addCarSalePost(carSalePost);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}")
                        .buildAndExpand(created.getId()).toUri()
        ).body(created);
    }

    @PutMapping("/{id}")
    public CarSalePost updateCarSalePost(@PathVariable String id, @Valid @RequestBody CarSalePost carSalePost, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid Car Sale Post data", getViolationsAsStringList(errors));
        }
        if (!id.equals(carSalePost.getId())) {
            throw new InvalidEntityDataException(
                    String.format("Car Sale Post URL ID:%s differs from body entity ID:%s", id, carSalePost.getId()));
        }
        return salePostsService.updateCarSalePost(carSalePost);
    }

    @DeleteMapping("/{id}")
    public CarSalePost deleteCarSalePost(@PathVariable String id) {
        return salePostsService.deleteCarSalePost(id);
    }


}
