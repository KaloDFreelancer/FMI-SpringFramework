package fmi.course.cookingwebapp.model;


import fmi.course.cookingwebapp.constant.AccountStatus;
import fmi.course.cookingwebapp.constant.Gender;
import fmi.course.cookingwebapp.constant.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    @NonNull
    @NotNull
    @Size(min = 2, max = 15)
    private String username;

    @NonNull
    @NotNull
    @Pattern(message = "Password must have at least 8 symbols from which 1 number and 1 special character", regexp = "(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}")
    private String password;

    @NonNull
    @NotNull
    private Gender gender;

    private Role role = Role.USER;

    private String imageUrl;

    @Size(max=512)
    private String shortDescription;

    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @CreatedDate
    private LocalDateTime created = LocalDateTime.now();

    @UpdateTimestamp
    private LocalDateTime updated;

    @OneToMany(mappedBy="author")
    private Set<Recipe> recipes;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        LinkedList<GrantedAuthority> authorities = new LinkedList<>();
        authorities.push(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }

    public User(@NonNull @NotNull @Size(min = 2, max = 30) String name, @NonNull @NotNull @Size(min = 2, max = 15) String username, @NonNull @NotNull @Size(min = 4) @Pattern(message = "Password must have at least 8 symbols from which 1 number and 1 special character", regexp = "(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}") String password,@NonNull @NotNull Gender gender, Role role, @NonNull @NotNull @Size(max = 512) String shortDescription) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.role = role;
        this.shortDescription = shortDescription;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountStatus.equals(AccountStatus.ACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountStatus.equals(AccountStatus.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return accountStatus.equals(AccountStatus.ACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return accountStatus.equals(AccountStatus.ACTIVE);
    }
}
