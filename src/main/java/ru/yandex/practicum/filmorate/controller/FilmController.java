package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> idToFilm = new HashMap<>();
    private long nextId = 0L;

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Film list requested");
        return new ArrayList<>(idToFilm.values());
    }

    @PostMapping
    public Film create(@RequestBody @Validated Film film) {
        long id = getNextId();
        film.setId(id);
        idToFilm.put(id, film);
        log.info("Added new film successfully: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Validated Film newFilm) {
        Long id = newFilm.getId();
        if (id == null || !idToFilm.containsKey(id)) {
            String message = "Film with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        idToFilm.put(id, newFilm);
        log.info("Updated film with id {} successfully: {}", id, newFilm);
        return newFilm;
    }

    private long getNextId() {
        return ++nextId;
    }
}
