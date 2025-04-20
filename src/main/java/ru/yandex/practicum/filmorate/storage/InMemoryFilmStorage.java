package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> idToFilm = new HashMap<>();

    @Override
    public void add(Film film) {
        idToFilm.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        Long id = film.getId();
        if (id == null || !idToFilm.containsKey(id)) {
            String message = "Film with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        idToFilm.put(id, film);
    }

    @Override
    public Film getById(long id) {
        return idToFilm.get(id);
    }

    @Override
    public Collection<Film> getAll() {
        return idToFilm.values();
    }
}
