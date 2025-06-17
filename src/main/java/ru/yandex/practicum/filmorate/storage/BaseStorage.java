package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface BaseStorage<T> {
    T add(T entity);

    T update(T entity);

    T getById(long id);

    Collection<T> getAll();
}
