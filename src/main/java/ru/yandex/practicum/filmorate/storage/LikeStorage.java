package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

public interface LikeStorage extends BaseStorage<Like> {
    boolean deleteByUserId(long filmId, long userId);
}
