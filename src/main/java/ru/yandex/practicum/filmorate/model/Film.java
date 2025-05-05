package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validator.DateAfter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

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

    @JsonIgnore
    private Set<Long> likes = new HashSet<>();

    private Collection<FilmGenre> genre;

    private FilmRating rating;
}
