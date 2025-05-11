package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage extends BaseStorage<Film> {
    List<Film> getFilmListByIds(Collection<Long> ids);

    List<Film> getMostPopular(long count);
}
