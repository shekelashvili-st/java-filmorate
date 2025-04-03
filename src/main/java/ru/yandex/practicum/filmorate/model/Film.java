package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.validator.DateAfter;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {

    private Long id;

    @NotEmpty(message = "Film name must not be empty/null")
    private String name;

    @Length(max = 200, message = "Film description must be shorter than 200 characters")
    private String description;

    @DateAfter(referenceDay = 28, referenceMonth = 12, referenceYear = 1895)
    private LocalDate releaseDate;

    @DurationMin(nanos = 1, message = "Film duration must be positive")
    private Duration duration;
}
