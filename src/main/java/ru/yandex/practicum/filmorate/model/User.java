package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

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
}
