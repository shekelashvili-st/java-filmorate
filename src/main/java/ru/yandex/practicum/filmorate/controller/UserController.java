package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> idToUser = new HashMap<>();
    private long nextId = 0L;

    @GetMapping()
    public Collection<User> getAll() {
        log.info("User list requested");
        return new ArrayList<>(idToUser.values());
    }

    @PostMapping
    public User create(@RequestBody @Validated User user) {
        long id = getNextId();
        user.setId(id);
        idToUser.put(id, user);
        log.info("Added new user successfully: {}", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Validated User newUser) {
        Long id = newUser.getId();
        if (id == null || !idToUser.containsKey(id)) {
            String message = "User with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        idToUser.put(id, newUser);
        log.info("Updated user with id {} successfully: {}", id, newUser);
        return newUser;
    }

    private long getNextId() {
        return ++nextId;
    }
}
