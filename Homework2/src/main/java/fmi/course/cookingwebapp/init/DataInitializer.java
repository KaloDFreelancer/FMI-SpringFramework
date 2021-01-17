package fmi.course.cookingwebapp.init;

import fmi.course.cookingwebapp.constant.Gender;
import fmi.course.cookingwebapp.constant.Role;
import fmi.course.cookingwebapp.model.User;
import fmi.course.cookingwebapp.service.RecipesService;
import fmi.course.cookingwebapp.service.UsersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final User admin = new User("Admin Name", "admin", "admin", Gender.MALE,Role.ADMIN, "Short description");
    private final RecipesService recipesService;
    private final UsersService usersService;

    public DataInitializer(RecipesService recipesService, UsersService usersService) {
        this.recipesService = recipesService;
        this.usersService = usersService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usersService.getCount() == 0) {
            usersService.addUser(admin);
        }
    }
}
