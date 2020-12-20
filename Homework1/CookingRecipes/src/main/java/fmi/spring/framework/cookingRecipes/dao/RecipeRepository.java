package fmi.spring.framework.cookingRecipes.dao;

import fmi.spring.framework.cookingRecipes.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

//@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    List<Recipe> findAllByKeywordsContaining(Iterable<String> keywords);
}
