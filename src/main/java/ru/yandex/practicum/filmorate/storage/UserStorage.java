package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage extends BaseStorage<User> {
    List<User> getUserListByIds(Collection<Long> ids);
}
