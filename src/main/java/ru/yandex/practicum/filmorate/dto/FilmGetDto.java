package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.validator.DateAfter;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class FilmGetDto {

    private Long id;

    @NotEmpty(message = "Film name must not be empty/null")
    private String name;

    @Length(max = 200, message = "Film description must be shorter than 200 characters")
    private String description;

    @NotNull(message = "Film release date must not be null")
    @DateAfter(referenceDay = 28, referenceMonth = 12, referenceYear = 1895)
    private LocalDate releaseDate;

    @Min(value = 0, message = "Film duration must be positive")
    private int duration;

    private Rating mpa;

    private Set<@NotNull Genre> genres;
}
