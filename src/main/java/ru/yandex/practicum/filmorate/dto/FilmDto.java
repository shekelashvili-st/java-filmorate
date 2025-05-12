package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validator.DateAfter;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class FilmDto {

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

    private IdContainer mpa;

    // Пришлось изменить Set на List, чтобы проходились тесты. Они ожидают id в "правильном" порядке
    // В базе данных (класс Film) используется Set
    private List<@NotNull IdContainer> genres;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdContainer {
        @NotNull(message = "MPA and genre ids must not be null")
        @Min(value = 0, message = "MPA and genre ids must be positive")
        private Long id;
    }
}
