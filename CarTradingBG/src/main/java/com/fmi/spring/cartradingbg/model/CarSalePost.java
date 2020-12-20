package com.fmi.spring.cartradingbg.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Document(collection = "car-sale-posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarSalePost {
    @Id
    @Pattern(regexp = "[A-Za-z0-9]{24}", message = "Invalid Recipe ID") // Mongodb ObjectID
    private String id;

    @Length(max = 30, min = 3, message = "Length should be between 3 and 30!")
    @NotBlank
    private String title;

    @Length(max = 20, min = 1, message = "Length should be between 1 and 20!")
    @NotBlank
    private String model;

    @Length(max = 20, min = 1, message = "Length should be between 1 and 20!")
    @NotBlank
    private String brand;

    @NotNull
    @Past(message = "Should be before current date!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate manufactureDate;

    @NotNull
    @PositiveOrZero(message = "Can't be negative!")
    private Long price;

    @Length(max = 1024, min = 3, message = "Length should be between 3 and 1024!")
    @NotBlank
    private String description;

    @NonNull
    @URL
    private String imageUrl;

    @NotNull
    @NonNull
    @Pattern(regexp = "[A-Za-z0-9]{24}", message = "Invalid seller ID")
    private String sellerId;

    private boolean statusFinished;

    public CarSalePost(@Length(max = 20, min = 3, message = "Length should be between 3 and 20!") @NotBlank String title, @Length(max = 20, min = 3, message = "Length should be between 3 and 20!") @NotBlank String model, @Length(max = 20, min = 3, message = "Length should be between 3 and 20!") @NotBlank String brand, @NotNull @Past(message = "Should be before current date!") LocalDate manufactureDate, @NotNull @PositiveOrZero(message = "Can't be negative!") Long price, @Length(max = 1024, min = 3, message = "Length should be between 3 and 1024!") @NotBlank String description, @NonNull @URL String imageUrl) {
        this.title = title;
        this.model = model;
        this.brand = brand;
        this.manufactureDate = manufactureDate;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
