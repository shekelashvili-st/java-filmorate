package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private Long id;

    @Email
    private String email;

    @NotBlank(message = "User login must not be empty/null")
    @Pattern(regexp = "^\\S*$", message = "User login must not contain whitespace")
    private String login;

    private String name;

    @Past(message = "User birthday must not be in the future")
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    @JsonCreator
    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = (name == null || name.isBlank()) ? login : name;
        this.birthday = birthday;
    }
}
