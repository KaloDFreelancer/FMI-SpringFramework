package com.fmi.spring.cartradingbg.init;

import com.fmi.spring.cartradingbg.model.CarSalePost;
import com.fmi.spring.cartradingbg.model.User;
import com.fmi.spring.cartradingbg.service.CarSalePostsService;
import com.fmi.spring.cartradingbg.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static com.fmi.spring.cartradingbg.enums.Role.*;


@Component
public class DataInitializer implements CommandLineRunner {
    private static final List<User> SAMPLE_USERS = List.of(
            new User("Default", "Admin", "admin", "admin",
                    "https://lh3.googleusercontent.com/proxy/cGNVbclL0E2LBuUnIxUaC7d-wP_K18xwNUMVzCHmtxgdEtaknGpKCZz-rBVUWP46jCXJSPq6Va7hZ__JZVjG4EKwLx-Kezlk9Qtb5NDc9Gb-E1oq85KV",
                    Set.of(BUYER, SELLER, ADMIN),
                    "Don't mess with the admin because he can do a lot of nasty things!"),
            new User("DefaultSeller", "Seller", "seller", "seller",
                    "https://cdn.iconscout.com/icon/premium/png-512-thumb/public-domain-user-618551.png",
                    Set.of(SELLER),
                    "Everyone wants to have a friend who sells cars!"),
            new User("DefaultBuyer", "BUYER", "buyer", "buyer",
                    "https://www.publicdomainpictures.net/pictures/230000/velka/computer-user.jpg",
                    Set.of(BUYER),
                    "This guy hopes that one day he could be able to buy something that isn't going to break in the middle of the road.")
    );
    private static final List<CarSalePost> SAMPLE_CarSalePostS = List.of(
            new CarSalePost("The BEST A3 ON THE MARKET", "A3", "AUDI", LocalDate.of(2000, Month.JULY, 4), 1L, "My first car ever!", "https://media.snimka.bg/s1/3144/032531982-big.jpg?r=0%5B/IMG%5D%5B/URL"),
            new CarSalePost("The BEST A5 ON THE MARKET", "A5", "AUDI", LocalDate.of(2000, Month.AUGUST, 4), 10000L, "My second car!", "https://ca-times.brightspotcdn.com/dims4/default/a5c6fc4/2147483647/strip/true/crop/2048x1152+0+0/resize/840x473!/quality/90/?url=https%3A%2F%2Fcalifornia-times-brightspot.s3.amazonaws.com%2F73%2F7f%2F60372d529c8b1672532247736fc9%2Fsd-1509139070-vj9ndal5pe-snap-image")
    );
    private final CarSalePostsService carSalePostsService;
    private final UserService userService;

    public DataInitializer(CarSalePostsService carSalePostsService, UserService userService) {
        this.carSalePostsService = carSalePostsService;
        this.userService = userService;
    }


    @Override
    public void run(String... args) throws Exception {
        if (userService.getCount() == 0) {
            SAMPLE_USERS.forEach(userService::addUser);
        }

        if (carSalePostsService.getCount() == 0) {
            SAMPLE_CarSalePostS.forEach(CarSalePost -> {
                CarSalePost.setSellerId(userService.getUserByUsername("admin").getId());
                carSalePostsService.addCarSalePost(CarSalePost);
            });
        }
    }
}
