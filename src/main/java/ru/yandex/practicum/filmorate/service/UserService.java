package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage storage;
    private long nextId = 0L;

    public Collection<User> getAll() {
        return storage.getAll();
    }

    public User create(User user) {
        long id = getNextId();
        user.setId(id);
        storage.add(user);
        log.info("Added new user successfully: {}", user);
        return user;
    }

    public User update(User updatedUser) {
        storage.update(updatedUser);
        log.info("Updated user with id {} successfully: {}", updatedUser.getId(), updatedUser);
        return updatedUser;
    }

    private long getNextId() {
        return ++nextId;
    }
}
