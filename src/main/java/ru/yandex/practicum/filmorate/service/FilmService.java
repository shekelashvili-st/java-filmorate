package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.mapper.FilmDtoMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.BaseStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final BaseStorage<Rating> ratingStorage;
    private final BaseStorage<Genre> genreStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmService(@Qualifier("H2FilmStorage") FilmStorage filmStorage,
                       @Qualifier("H2UserStorage") UserStorage userStorage,
                       @Qualifier("H2RatingStorage") BaseStorage<Rating> ratingStorage,
                       @Qualifier("H2GenreStorage") BaseStorage<Genre> genreStorage,
                       @Qualifier("H2LikeStorage") LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.ratingStorage = ratingStorage;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
    }

    public Collection<FilmDto> getAll() {
        return filmStorage.getAll().stream().map(FilmDtoMapper::mapToFilmDto).toList();
    }

    public FilmDto getById(long id) {
        return FilmDtoMapper.mapToFilmDto(filmStorage.getById(id));
    }

    public FilmDto create(FilmDto filmDto) {
        Film inputFilm = FilmDtoMapper.mapToFilm(filmDto);
        //Check if rating and genre ids exist
        Long ratingId = inputFilm.getRatingId();
        if (ratingId != null) {
            ratingStorage.getById(ratingId);
        }
        for (Long genreId : inputFilm.getGenreIds()) {
            genreStorage.getById(genreId);
        }

        Film newFilm = filmStorage.add(inputFilm);
        log.info("Added new film successfully: {}", newFilm);
        filmDto.setId(newFilm.getId());
        return filmDto;
    }

    public FilmDto update(FilmDto updatedFilm) {
        filmStorage.getById(updatedFilm.getId());
        Film filmInStorage = filmStorage.update(FilmDtoMapper.mapToFilm(updatedFilm));
        log.info("Updated film with id {} successfully: {}", filmInStorage.getId(), filmInStorage);
        return updatedFilm;
    }

    public void addLike(long filmId, long userId) {
        // Also used to check for existence
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        likeStorage.add(new Like(null, filmId, userId));
        log.info("Added like from user {} on film {} successfully", user, film);
    }

    public void removeLike(long filmId, long userId) {
        // Also used to check for user existence
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);
        boolean hadLikeFromUser = likeStorage.deleteByUserId(filmId, userId);
        log.debug("Film {} had like from user {} before removal: {}", film, user, hadLikeFromUser);
        log.info("Removed like from user {} on film {} successfully", user, film);
    }

    public List<FilmDto> getMostLiked(long maxSize) {
        List<Film> films = filmStorage.getMostPopular(maxSize);
        return films.stream().map(FilmDtoMapper::mapToFilmDto).toList();
    }
}
