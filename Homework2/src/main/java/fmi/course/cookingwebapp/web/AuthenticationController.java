package fmi.course.cookingwebapp.web;

import fmi.course.cookingwebapp.model.User;
import fmi.course.cookingwebapp.service.UsersService;
import fmi.course.cookingwebapp.util.ErrorHandlingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
public class AuthenticationController {
    private static final String IMAGE_UPLOADS_DIR = "tmp";

    private final UsersService usersService;

    public AuthenticationController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/registration")
    public ModelAndView getRegistrationPage(ModelMap model) {
        model.addAttribute("user", new User());
        return new ModelAndView("registration");
    }

    @PostMapping("/registration")
    public String addUser(@Valid @ModelAttribute("user") User user,
                          BindingResult errors,
                          @RequestParam("file") MultipartFile file,
                          Model model) {
        if(errors.hasErrors()) {
            List<String> errorMessages = errors.getAllErrors().stream().map(err -> {
                ConstraintViolation cv = err.unwrap(ConstraintViolation.class);
                return String.format("Error in '%s' - invalid value: '%s'", cv.getPropertyPath(), cv.getInvalidValue());
            }).collect(Collectors.toList());

            model.addAttribute("myErrors", errorMessages);
            model.addAttribute("fileError", null);
            return "registration";
        }
        else {
            if (!file.isEmpty() && file.getOriginalFilename().length() > 0) {
                if (Pattern.matches("\\w+\\.(jpg|png)", file.getOriginalFilename())) {
                    handleMultipartFile(file);
                    user.setImageUrl(file.getOriginalFilename());
                } else {
                    model.addAttribute("myErrors", null);
                    model.addAttribute("fileError", "Submit picture [.jpg, .png]");
                    return "registration";
                }
            }
            usersService.addUser(user);
            return "redirect:/login";
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
