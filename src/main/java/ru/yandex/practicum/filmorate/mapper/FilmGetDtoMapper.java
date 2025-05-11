package ru.yandex.practicum.filmorate.mapper;


import ru.yandex.practicum.filmorate.dto.FilmGetDto;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FilmGetDtoMapper {
    public static FilmGetDto mapToFilmGetDto(Film film, Collection<Genre> dbGenres, Collection<Rating> dbRatings) {
        Set<Genre> genres = new HashSet<>();
        Long ratingId = film.getRatingId();
        for (Long genreId : film.getGenreIds()) {
            Genre foundGenre = dbGenres.stream()
                    .filter(genre -> Objects.equals(genre.getId(), genreId))
                    .findFirst()
                    .orElseThrow(() -> new InternalServerException("Genre not found"));
            genres.add(foundGenre);
        }

        return FilmGetDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .genres(genres)
                .mpa(ratingId == null ? null : dbRatings.stream()
                        .filter(rating -> Objects.equals(rating.getId(), ratingId))
                        .findFirst()
                        .orElseThrow(() -> new InternalServerException("Rating not found")))
                .build();
    }

}
