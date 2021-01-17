package fmi.course.cookingwebapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @NonNull
    @NotNull
    @Size(min = 2, max = 80)
    private String name;

    @NonNull
    @NotNull
    @Size(min = 2, max = 256)
    private String shortDescription;

    private Integer timeNeededForCooking;

    @ElementCollection
    private List<@Pattern(regexp = "^[\\w\\s-]+$", message = "Invalid product name - should contain only letters and digits") String> products;

    private String imageUrl;

    @NonNull
    @NotNull
    @Size(min = 2, max = 2048)
    private String fullDescription;

    @ElementCollection
    private List<@Pattern(regexp = "^[\\w\\s-]+$", message = "Invalid tag - should contain only letters and digits") String> tags;

    @CreatedDate
    private LocalDateTime created = LocalDateTime.now();

    @UpdateTimestamp
    private LocalDateTime updated;


}
