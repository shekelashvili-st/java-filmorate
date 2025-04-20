package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage storage;
    private long nextId = 0L;

    public Collection<Film> getAll() {
        return storage.getAll();
    }

    public Film create(Film film) {
        long id = getNextId();
        film.setId(id);
        storage.add(film);
        log.info("Added new film successfully: {}", film);
        return film;
    }

    public Film update(Film updatedFilm) {
        storage.update(updatedFilm);
        log.info("Updated film with id {} successfully: {}", updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }

    private long getNextId() {
        return ++nextId;
    }
}
