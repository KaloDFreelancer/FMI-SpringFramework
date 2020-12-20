package com.fmi.spring.cartradingbg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fmi.spring.cartradingbg.enums.Role;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Document(collection="users")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @Pattern(regexp = "[A-Za-z0-9]{24}") // Mongodb ObjectID
    private String id;
    @NonNull
    @NotNull
    @Size(min=2, max=30)
    private String firstName;
    @NonNull
    @NotNull
    @Size(min=2, max=30)
    private String lastName;
    @NonNull
    @NotNull
    @Size(min=1, max=15)
    @Pattern(message = "Only letters are allowed for username",regexp = "^[a-zA-Z]*$") // only word characters
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NonNull
    @Pattern(message = "Password must have at least 8 symbols from which 1 number and 1 special character",regexp = "(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}")
    private String password;
    @NonNull
    @URL
    private String imageUrl;

    private boolean active = true;
    private Set<Role> roles = Set.of(Role.BUYER, Role.SELLER);
    @PastOrPresent
    private LocalDateTime created = LocalDateTime.now();
    @Size(max = 512)
    private String description;


    public User(@NonNull @NotNull @Size(min = 2, max = 30) String firstName,
                @NonNull @NotNull @Size(min = 2, max = 30) String lastName,
                @NonNull @NotNull @Size(min = 2, max = 30) String username,
                @NonNull @Size(min = 6) String password,
                @NonNull @URL String imageUrl, Set<Role> roles,
                @Size(max = 512) String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.imageUrl = imageUrl;
        this.roles = roles;
        this.description = description;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString()))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return active;
    }
}
