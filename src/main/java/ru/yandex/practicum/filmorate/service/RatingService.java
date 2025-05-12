package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;

@Service
@Slf4j
public class RatingService {
    private final BaseStorage<Rating> ratingStorage;

    @Autowired
    public RatingService(@Qualifier("H2RatingStorage") BaseStorage<Rating> ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public Collection<Rating> getAll() {
        return ratingStorage.getAll();
    }

    public Rating getById(long id) {
        return ratingStorage.getById(id);
    }
}
