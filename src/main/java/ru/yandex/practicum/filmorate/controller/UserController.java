package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public Collection<User> getAll() {
        log.info("User list requested");
        return userService.getAll();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Request to add user received: {}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User newUser) {
        log.info("Request to update user with id {} received: {}", newUser.getId(), newUser);
        return userService.update(newUser);
    }
}
