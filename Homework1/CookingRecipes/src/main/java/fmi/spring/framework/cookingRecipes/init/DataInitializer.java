package fmi.spring.framework.cookingRecipes.init;

import fmi.spring.framework.cookingRecipes.model.Recipe;
import fmi.spring.framework.cookingRecipes.model.User;
import fmi.spring.framework.cookingRecipes.service.RecipeService;
import fmi.spring.framework.cookingRecipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static fmi.spring.framework.cookingRecipes.model.Role.*;


@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    private static final List<User> SAMPLE_USERS = List.of(
            new User("Default", "Admin", "admin", "admin",
                    "https://lh3.googleusercontent.com/proxy/cGNVbclL0E2LBuUnIxUaC7d-wP_K18xwNUMVzCHmtxgdEtaknGpKCZz-rBVUWP46jCXJSPq6Va7hZ__JZVjG4EKwLx-Kezlk9Qtb5NDc9Gb-E1oq85KV",
                    Set.of(READER, AUTHOR, ADMIN),
                    "Don't mess with the admin because he can do a lot of nasty things!"),
            new User("Default", "Author", "author", "author",
                    "https://cdn.iconscout.com/icon/premium/png-512-thumb/public-domain-user-618551.png",
                    Set.of(AUTHOR),
                    "Everyone wants to have a recipe writer in home!"),
            new User("Default", "Reader", "reader", "reader",
                    "https://www.publicdomainpictures.net/pictures/230000/velka/computer-user.jpg",
                    Set.of(READER),
                    "This guy hopes that one day he could be able to cook 5% of the dishes he sees.")
    );
    private static final List<Recipe> SAMPLE_RecipeS = List.of(
            new Recipe("Chef John's Best Recipes for Thanksgiving Leftovers",
                    "Enjoy this diverse selection of recipes for using up your Thanksgiving leftovers!",
                    "You do not want to miss Thanksgiving, the Sequel, featuring recipes that are arguably bigger and better than the originals. That's because Chef John knows the secret to a great Thanksgiving leftover recipe: \"Lots of flavor, not a lot of ingredients, and the ability to stand on its own rather than remind us of the meal it came from.\"",5, Arrays.asList("musaka","cheese"),
                    "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F43%2F2020%2F11%2F19%2F6019680_turkey-tamale-pie_chef-john-2000.jpg&q=85",
                    List.of("thanksgiving", "leftovers")),
            new Recipe("Chocolate Crinkles", "Chocolate cookies coated in confectioners' sugar...very good!",
                    "58 calories; protein 0.9g 2% DV; carbohydrates 9.8g 3% DV; fat 2g 3% DV; cholesterol 10.3mg 3% DV; sodium 33.9mg 1% DV.",6, Arrays.asList("cocoa powder","eggs"),
                    "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fimages.media-allrecipes.com%2Fuserphotos%2F430444.jpg&w=272&h=272&c=sc&poi=face&q=85",
                    List.of("dessert", "chocolate", "crinkles")),
            new Recipe("Old-Fashioned Swedish Glogg", "My grandfather brought this recipe over from Sweden in 1921. We still use it today. God Jul.",
                    "162 calories; protein 0.9g 2% DV; carbohydrates 10.4g 3% DV; fat 1.5g 2% DV; cholesterol 0mg; sodium 4.4mg. ",7, Arrays.asList("wine","whiskey"),
                    "https://www.publicdomainpictures.net/pictures/40000/velka/spring-flowers-13635086725Z1.jpg",
                    List.of("swedish", "glogg"))
    );


    @Override
    public void run(String... args) throws Exception {
        if (userService.getCount() == 0) {
            SAMPLE_USERS.forEach(userService::addUser);
        }

        if (recipeService.getCount() == 0) {
            SAMPLE_RecipeS.forEach(Recipe -> {
                Recipe.setAuthorId(userService.getUserByUsername("author").getId());
                recipeService.addRecipe(Recipe);
            });
        }
    }
}
