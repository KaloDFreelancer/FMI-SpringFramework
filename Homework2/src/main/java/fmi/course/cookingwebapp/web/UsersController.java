package fmi.course.cookingwebapp.web;

import fmi.course.cookingwebapp.model.Recipe;
import fmi.course.cookingwebapp.model.User;
import fmi.course.cookingwebapp.service.RecipesService;
import fmi.course.cookingwebapp.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
public class UsersController {
    private static final String IMAGE_UPLOADS_DIR = "tmp";

    private final UsersService usersService;

    private final RecipesService recipesService;

    public UsersController(UsersService usersService, RecipesService recipesService) {
        this.usersService = usersService;
        this.recipesService = recipesService;
    }

    @GetMapping("/users")
    public ModelAndView getUsersPage(ModelMap model) {
        model.addAttribute("usersList", usersService.getAllUsers());
        return new ModelAndView("users");
    }

    @RequestMapping("/users/delete/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        recipesService.removeRecipesWithAuthorId(id);
        usersService.deleteUser(id);
        return "redirect:/users";
    }

    @RequestMapping("/users/edit/{id}")
    public ModelAndView editUserById(@PathVariable("id") Long id, ModelMap modelMap) {
        User userById = usersService.getUserById(id);
        modelMap.addAttribute("user", usersService.getUserById(id));
        return new ModelAndView("edit_user");
    }

    @PostMapping("/users/edit/{id}")
    public String editUserByIdPost(@Valid @ModelAttribute("user") User user,
                                         BindingResult errors,
                                         @RequestParam("file") MultipartFile file,
                                         Model model,
                                         Authentication authentication) {
        if(errors.hasErrors()) {
            List<String> errorMessages = errors.getAllErrors().stream().map(err -> {
                ConstraintViolation cv = err.unwrap(ConstraintViolation.class);
                return String.format("Error in '%s' - invalid value: '%s'", cv.getPropertyPath(), cv.getInvalidValue());
            }).collect(Collectors.toList());

            model.addAttribute("myErrors", errorMessages);
            model.addAttribute("fileError", null);
            return "users/edit/{id}";
        }
        else {
            if (!file.isEmpty() && file.getOriginalFilename().length() > 0) {
                if (Pattern.matches("\\w+\\.(jpg|png)", file.getOriginalFilename())) {
                    handleMultipartFile(file);
                    user.setImageUrl(file.getOriginalFilename());
                } else {
                    model.addAttribute("myErrors", null);
                    model.addAttribute("fileError", "Submit picture [.jpg, .png]");
                    return "users/edit/{id}";
                }
            }
            usersService.updateUser(user);
            return "redirect:/users";
        }
    }


    private void handleMultipartFile(MultipartFile file) {
        try {
            File currentDir = new File(IMAGE_UPLOADS_DIR);
            if(!currentDir.exists()) {
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
