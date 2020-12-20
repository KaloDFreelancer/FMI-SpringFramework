package fmi.spring.framework.cookingRecipes.service;


import fmi.spring.framework.cookingRecipes.model.Recipe;

import java.util.List;
import java.util.Set;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    List<Recipe> getAllRecipesByKeywords(Set<String> keywords);
    Recipe getRecipeById(String id);
    Recipe addRecipe(Recipe Recipe);
    Recipe updateRecipe(Recipe Recipe);
    Recipe deleteRecipe(String id);
    long getCount();
}
