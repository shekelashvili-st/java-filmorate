package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

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

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {
        log.info("Request to get film with id {} received", id);
        return filmService.getById(id);
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

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        log.info("Request to add like from user with id {} to film with id {} received", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable long filmId, @PathVariable long userId) {
        log.info("Request to remove like from user with id {} on film with id {} received", filmId, userId);
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") @Min(0) long count) {
        log.info("Request to get {} films with most likes received", count);
        return filmService.getMostLiked(count);
    }
}
