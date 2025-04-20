package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void add(Film film);

    void update(Film film);

    Film getById(long id);

    Collection<Film> getAll();
}
