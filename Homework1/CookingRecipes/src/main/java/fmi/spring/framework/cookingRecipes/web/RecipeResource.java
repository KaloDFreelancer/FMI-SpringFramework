package fmi.spring.framework.cookingRecipes.web;

import fmi.spring.framework.cookingRecipes.exception.InvalidEntityDataException;
import fmi.spring.framework.cookingRecipes.exception.NonexistingEntityException;
import fmi.spring.framework.cookingRecipes.model.Recipe;
import fmi.spring.framework.cookingRecipes.service.RecipeService;
import fmi.spring.framework.cookingRecipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;

import static fmi.spring.framework.cookingRecipes.util.ErrorHandlingUtils.getViolationsAsStringList;


@RestController
@RequestMapping("/api/recipes")
public class RecipeResource {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Recipe> getAllRecipes(@RequestParam(name = "keywords", required = false) String keywordStr) {
        if (keywordStr != null && !keywordStr.trim().isEmpty()) {
            Set<String> keywords = Set.of(keywordStr.trim().split(",\\s*"));
            return recipeService.getAllRecipesByKeywords(keywords);
        } else {
            return recipeService.getAllRecipes();
        }

    }

    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable("id") String id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(Principal principal, @Valid @RequestBody Recipe Recipe, Errors errors) {
        String principalName = principal.getName();
        String RecipeAuthorId = Recipe.getAuthorId();
        if (!principalName.equals(userService.getUserById(RecipeAuthorId).getUsername())) {
            throw new NonexistingEntityException("Author can't create/edit/delete other people's Recipes");
        }
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid Recipe data", getViolationsAsStringList(errors));
        }
        Recipe created = recipeService.addRecipe(Recipe);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}")
                        .buildAndExpand(created.getId()).toUri()
        ).body(created);
    }

    @PutMapping("/{id}")
    public Recipe updateRecipe(@PathVariable String id, @Valid @RequestBody Recipe Recipe, Principal principal, Errors errors) {
        String principalName = principal.getName();
        String RecipeAuthorId = getRecipeById(id).getAuthorId();
        if (!principalName.equals(userService.getUserById(RecipeAuthorId).getUsername())) {
            throw new NonexistingEntityException("Author can't create/edit/delete other people's Recipes");
        }
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid Recipe data", getViolationsAsStringList(errors));
        }
        if (!id.equals(Recipe.getId())) {
            throw new InvalidEntityDataException(
                    String.format("Recipe URL ID:%s differs from body entity ID:%s", id, Recipe.getId()));
        }
        return recipeService.updateRecipe(Recipe);
    }


    @DeleteMapping("/{id}")
    public Recipe deleteRecipe(@PathVariable String id, Principal principal) {
        String principalName = principal.getName();
        String recipeAuthorId = getRecipeById(id).getAuthorId();
        if (!principalName.equals(userService.getUserById(recipeAuthorId).getUsername())) {
            throw new NonexistingEntityException("Author can't create/edit/delete other people's Recipes");
        }
        Recipe removed = getRecipeById(id);
        recipeService.deleteRecipe(id);
        return removed;
    }

}
