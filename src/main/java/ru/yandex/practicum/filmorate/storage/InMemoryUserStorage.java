package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryUserStorage implements BaseStorage<User> {

    private final Map<Long, User> idToUser = new HashMap<>();
    private long nextId = 0L;

    @Override
    public User add(User user) {
        long id = getNextId();
        user.setId(id);
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
        User oldUser = idToUser.get(id);
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

    private long getNextId() {
        return ++nextId;
    }
}
