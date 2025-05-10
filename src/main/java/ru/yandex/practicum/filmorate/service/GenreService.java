package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;

@Service
@Slf4j
public class GenreService {
    private final BaseStorage<Genre> genreStorage;

    @Autowired
    public GenreService(@Qualifier("H2GenreStorage") BaseStorage<Genre> genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getAll() {
        return genreStorage.getAll();
    }

    public Genre getById(long id) {
        return genreStorage.getById(id);
    }
}
