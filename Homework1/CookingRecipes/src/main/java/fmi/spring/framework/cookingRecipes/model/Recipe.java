package fmi.spring.framework.cookingRecipes.model;

import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection="recipes")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @Pattern(regexp = "[A-Za-z0-9]{24}", message = "Invalid Recipe ID") // Mongodb ObjectID
    private String id;
    @NonNull
    @NotNull
    @Size(min=2, max=80)
    private String title;
    @NonNull
    @NotNull
    @Size(min=2, max=2048)
    private String content;
    @NonNull
    @NotNull
    @Size(min=2, max=256)
    private String shortDescription;
    @Positive
    private Integer timeNeeded;
    private List<@Pattern(regexp = "^[\\w\\s-]+$", message = "Invalid product - should contain only letters and digits") String> products = new ArrayList<>();
    @NonNull
    @URL
    private String imageUrl;
    @NotNull
    @NonNull
    @Pattern(regexp = "[A-Za-z0-9]{24}", message = "Invalid author ID")
    private String authorId;
    private List<@Pattern(regexp = "^[\\w\\s-]+$", message = "Invalid keyword - should contain only letters and digits") String> keywords = new ArrayList<>();
    @PastOrPresent
    private LocalDateTime created = LocalDateTime.now();
    @PastOrPresent
    @LastModifiedDate
    private LocalDateTime modified = LocalDateTime.now();

    public Recipe(@NonNull String title, @NonNull String content,@NonNull String shortDescription,@Positive Integer timeNeeded,List<String> products, @NonNull String imageUrl, List<String> keywords) {
        this.title = title;
        this.content = content;
        this.shortDescription = shortDescription;
        this.timeNeeded = timeNeeded;
        this.products = products;
        this.imageUrl = imageUrl;
        this.keywords = keywords;
    }
}
