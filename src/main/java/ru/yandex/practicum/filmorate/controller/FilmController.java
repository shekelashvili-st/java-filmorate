package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Film list requested");
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Request to add film received: {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film newFilm) {
        log.info("Request to update film with id {} received: {}", newFilm.getId(), newFilm);
        return filmService.update(newFilm);
    }
}
