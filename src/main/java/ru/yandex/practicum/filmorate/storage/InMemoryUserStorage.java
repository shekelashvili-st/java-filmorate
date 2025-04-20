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
    public User add(User user) {
        idToUser.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        if (id == null || !idToUser.containsKey(id)) {
            String message = "User with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        // Assuming we don't update the friend list
        User oldUser = idToUser.get(user.getId());
        updateUserFields(oldUser, user);
        return oldUser;
    }

    @Override
    public User getById(long id) {
        User user = idToUser.get(id);
        if (user == null) {
            String message = "User with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return idToUser.values();
    }

    private void updateUserFields(User oldUser, User user) {
        oldUser.setName(user.getName());
        oldUser.setLogin(user.getLogin());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setEmail(user.getEmail());
    }
}
