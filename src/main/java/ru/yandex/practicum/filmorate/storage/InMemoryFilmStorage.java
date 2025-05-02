package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> idToFilm = new HashMap<>();
    private long nextId = 0L;

    @Override
    public Film add(Film film) {
        long id = getNextId();
        film.setId(id);
        idToFilm.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Long id = film.getId();
        if (id == null || !idToFilm.containsKey(id)) {
            String message = "Film with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        // Assuming we don't update the likes
        Film oldFilm = idToFilm.get(id);
        updateFilmFields(oldFilm, film);
        return oldFilm;
    }

    @Override
    public Film getById(long id) {
        Film film = idToFilm.get(id);
        if (film == null) {
            String message = "Film with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return idToFilm.values();
    }

    private void updateFilmFields(Film oldFilm, Film film) {
        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());
    }

    private long getNextId() {
        return ++nextId;
    }
}
