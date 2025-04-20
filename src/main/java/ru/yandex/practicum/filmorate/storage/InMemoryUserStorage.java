package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> idToUser = new HashMap<>();

    @Override
    public void add(User user) {
        idToUser.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        Long id = user.getId();
        if (id == null || !idToUser.containsKey(id)) {
            String message = "User with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        idToUser.put(user.getId(), user);
    }

    @Override
    public User getById(long id) {
        return idToUser.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return idToUser.values();
    }
}
