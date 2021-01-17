package fmi.course.cookingwebapp.web;

import fmi.course.cookingwebapp.model.Recipe;
import fmi.course.cookingwebapp.model.User;
import fmi.course.cookingwebapp.service.RecipesService;
import fmi.course.cookingwebapp.service.UsersService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class RecipesController {
    private static final String IMAGE_UPLOADS_DIR = "tmp";

    private final RecipesService recipesService;

    private final UsersService usersService;


    public RecipesController(RecipesService recipesService, UsersService usersService) {
        this.recipesService = recipesService;
        this.usersService = usersService;
    }

    @GetMapping("/recipes")
    public ModelAndView getRecipesPage(ModelMap modelMap) {
        modelMap.addAttribute("recipesList", recipesService.getAllRecipes());
        return new ModelAndView("recipes");
    }

    @GetMapping("/recipes/new")
    public ModelAndView getNewRecipePage(ModelMap modelMap) {
        modelMap.addAttribute("recipe", new Recipe());
        return new ModelAndView("new_recipe");
    }

    @PostMapping("/recipes/new")
    public String addRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,
                            BindingResult errors,
                            @RequestParam("file") MultipartFile file,
                            Model model,
                            Authentication authentication) {

        if (errors.hasErrors()) {
            List<String> errorMessages = errors.getAllErrors().stream().map(err -> {
                ConstraintViolation cv = err.unwrap(ConstraintViolation.class);
                return String.format("Error in '%s' - invalid value: '%s'", cv.getPropertyPath(), cv.getInvalidValue());
            }).collect(Collectors.toList());

            model.addAttribute("myErrors", errorMessages);
            model.addAttribute("fileError", null);
            return "new_recipe";
        } else {
            if (!file.isEmpty() && file.getOriginalFilename().length() > 0) {
                if (Pattern.matches("\\w+\\.(jpg|png)", file.getOriginalFilename())) {
                    handleMultipartFile(file);
                    recipe.setImageUrl(file.getOriginalFilename());
                } else {
                    model.addAttribute("myErrors", null);
                    model.addAttribute("fileError", "Submit picture [.jpg, .png]");
                    return "new_recipe";
                }
            }
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User author = usersService.findByUsername(userDetails.getUsername());
            recipe.setAuthor(author);
            recipesService.addRecipe(recipe);
            return "redirect:/recipes";
        }
    }

    @RequestMapping("/recipes/delete/{id}")
    public String deleteRecipeById(@PathVariable("id") Long id, ModelMap modelMap) {
        recipesService.removeRecipeById(id);
        return "redirect:/recipes";
    }

    @GetMapping("/recipes/edit/{id}")
    public ModelAndView editPageOfRecipe(@PathVariable("id") Long id, ModelMap modelMap) {
        modelMap.addAttribute("recipe", recipesService.getRecipeById(id));

        return new ModelAndView("edit_recipe");

    }

    @PostMapping("/recipes/edit/{id}")
    public String editPageOfRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,
                                   BindingResult errors,
                                   @RequestParam("file") MultipartFile file,
                                   Model model,
                                   Authentication authentication) {
        if (errors.hasErrors()) {
            List<String> errorMessages = errors.getAllErrors().stream().map(err -> {
                ConstraintViolation cv = err.unwrap(ConstraintViolation.class);
                return String.format("Error in '%s' - invalid value: '%s'", cv.getPropertyPath(), cv.getInvalidValue());
            }).collect(Collectors.toList());

            model.addAttribute("myErrors", errorMessages);
            model.addAttribute("fileError", null);
            return "recipes/edit/{id}";
        } else {
            if (!file.isEmpty() && file.getOriginalFilename().length() > 0) {
                if (Pattern.matches("\\w+\\.(jpg|png)", file.getOriginalFilename())) {
                    handleMultipartFile(file);
                    recipe.setImageUrl(file.getOriginalFilename());
                } else {
                    model.addAttribute("myErrors", null);
                    model.addAttribute("fileError", "Submit picture [.jpg, .png]");
                    return "recipes/edit/{id}";
                }
            }
            Recipe recipe2 = recipesService.getRecipeById(recipe.getId());
            recipe.setAuthor(recipe2.getAuthor());
            recipesService.updateRecipe(recipe);
            return "redirect:/recipes";
        }
    }

    private void handleMultipartFile(MultipartFile file) {
        try {
            File currentDir = new File(IMAGE_UPLOADS_DIR);
            if (!currentDir.exists()) {
                currentDir.mkdirs();
            }
            String path = currentDir.getAbsolutePath() + "/" + file.getOriginalFilename();
            path = new File(path).getAbsolutePath();
            File f = new File(path);
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(f));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
