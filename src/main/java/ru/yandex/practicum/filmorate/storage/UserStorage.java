package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    void add(User user);

    void update(User user);

    User getById(long id);

    Collection<User> getAll();
}
