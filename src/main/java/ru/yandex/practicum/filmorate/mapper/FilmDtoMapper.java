package ru.yandex.practicum.filmorate.mapper;


import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class FilmDtoMapper {
    public static FilmDto mapToFilmDto(Film film) {
        List<FilmDto.IdContainer> genres = new ArrayList<>();
        Long ratingId = film.getRatingId();
        for (Long genreId : film.getGenreIds()) {
            genres.add(new FilmDto.IdContainer(genreId));
        }

        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .genres(genres)
                .mpa(ratingId == null ? null : new FilmDto.IdContainer(ratingId))
                .build();
    }

    public static Film mapToFilm(FilmDto filmDto) {
        List<FilmDto.IdContainer> genres = filmDto.getGenres();
        FilmDto.IdContainer rating = filmDto.getMpa();
        return Film.builder()
                .id(filmDto.getId())
                .name(filmDto.getName())
                .description(filmDto.getDescription())
                .releaseDate(filmDto.getReleaseDate())
                .duration(filmDto.getDuration())
                .ratingId(rating == null ? null : rating.getId())
                .genreIds(genres == null ? new HashSet<>() : genres.stream()
                        .map(FilmDto.IdContainer::getId)
                        .collect(Collectors.toSet()))
                .build();
    }
}
