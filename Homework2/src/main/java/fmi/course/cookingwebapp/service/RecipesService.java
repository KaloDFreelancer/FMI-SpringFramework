package fmi.course.cookingwebapp.service;

import fmi.course.cookingwebapp.model.Recipe;

import java.util.List;

public interface RecipesService {
    Iterable<Recipe> getAllRecipes();
    void removeRecipesWithAuthorId(Long id);
    void addRecipe(Recipe recipe);
    void removeRecipeById(Long id);
    Recipe getRecipeById(Long id);
    void updateRecipe(Recipe recipe);
}
