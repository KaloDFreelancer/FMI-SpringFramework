package fmi.course.cookingwebapp.service.implementation;

import fmi.course.cookingwebapp.exception.NonExistingEntityException;
import fmi.course.cookingwebapp.model.Recipe;
import fmi.course.cookingwebapp.repo.RecipeRepository;
import fmi.course.cookingwebapp.service.RecipesService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipesService {
    private final RecipeRepository recipeRepository;


    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Iterable<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public void removeRecipesWithAuthorId(Long id) {
        ArrayList<Recipe> all = (ArrayList<Recipe>) recipeRepository.findAll();
        all.stream().filter(x -> x.getAuthor().getId().equals(id)).forEach(recipeRepository::delete);
    }

    @Override
    public void addRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    @Override
    public void removeRecipeById(Long id) {
        recipeRepository.deleteById(id);
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new NonExistingEntityException(String.format("Recipe with id: %s does not exist.", id)));
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }
}
