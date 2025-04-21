package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage storage;
    private final UserStorage userStorage;
    private long nextId = 0L;

    public Collection<Film> getAll() {
        return storage.getAll();
    }

    public Film getById(long id) {
        return storage.getById(id);
    }

    public Film create(Film film) {
        long id = getNextId();
        film.setId(id);
        Film newFilm = storage.add(film);
        log.info("Added new film successfully: {}", newFilm);
        return newFilm;
    }

    public Film update(Film updatedFilm) {
        Film filmInStorage = storage.update(updatedFilm);
        log.info("Updated film with id {} successfully: {}", filmInStorage.getId(), filmInStorage);
        return filmInStorage;
    }

    public void addLike(long filmId, long userId) {
        Film film = storage.getById(filmId);
        // Also used to check for user existence
        User user = userStorage.getById(userId);
        Set<Long> likes = film.getLikes();
        likes.add(userId);
        log.info("Added like from user {} on film {} successfully", user, film);
    }

    public void removeLike(long filmId, long userId) {
        Film film = storage.getById(filmId);
        // Also used to check for user existence
        User user = userStorage.getById(userId);
        Set<Long> likes = film.getLikes();
        boolean hadLikeFromUser = likes.remove(userId);
        log.debug("Film {} had like from user {} before removal: {}", film, user, hadLikeFromUser);
        log.info("Removed like from user {} on film {} successfully", user, film);
    }

    public List<Film> getMostLiked(long maxSize) {
        return storage.getAll().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(maxSize)
                .toList();
    }

    private long getNextId() {
        return ++nextId;
    }
}
