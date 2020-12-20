package fmi.spring.framework.cookingRecipes.service.impl;

import fmi.spring.framework.cookingRecipes.dao.RecipeRepository;
import fmi.spring.framework.cookingRecipes.exception.NonexistingEntityException;
import fmi.spring.framework.cookingRecipes.model.Recipe;
import fmi.spring.framework.cookingRecipes.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {
    private RecipeRepository RecipeRepo;

    @Autowired
    public RecipeServiceImpl(RecipeRepository RecipeRepo) {
        this.RecipeRepo = RecipeRepo;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return RecipeRepo.findAll();
    }

    @Override
    public List<Recipe> getAllRecipesByKeywords(Set<String> keywords) {
        return RecipeRepo.findAllByKeywordsContaining(keywords);
    }

    @Override
    public Recipe getRecipeById(String id) {
        return RecipeRepo.findById(id).orElseThrow(() ->
            new NonexistingEntityException(String.format("Recipe with ID:%s does not exist.", id)));
    }

    @Override
    public Recipe addRecipe(Recipe Recipe) {
        Recipe.setId(null);
        return RecipeRepo.insert(Recipe);
    }

    @Override
    public Recipe updateRecipe(Recipe Recipe) {
        getRecipeById(Recipe.getId());
        Recipe.setModified(LocalDateTime.now());
        return RecipeRepo.save(Recipe);
    }

    @Override
    public Recipe deleteRecipe(String id) {
        Recipe removed = getRecipeById(id);
        RecipeRepo.deleteById(id);
        return removed;
    }

    @Override
    public long getCount() {
        return RecipeRepo.count();
    }
}
