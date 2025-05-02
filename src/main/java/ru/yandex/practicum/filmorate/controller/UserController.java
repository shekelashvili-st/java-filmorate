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

    @GetMapping
    public Collection<User> getAll() {
        log.info("User list requested");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@RequestParam long id) {
        log.info("Request to get user with id {} received", id);
        return userService.getById(id);
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

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriends(@PathVariable long userId, @PathVariable long friendId) {
        log.info("Request to assign users with ids {} and {} as friends received", userId, friendId);
        userService.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriends(@PathVariable long userId, @PathVariable long friendId) {
        log.info("Request to unassign users with ids {} and {} as friends received", userId, friendId);
        userService.removeFromFriends(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getFriends(@PathVariable long userId) {
        log.info("Requested friend list of user with id = {}", userId);
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public Collection<User> getCommonFriends(@PathVariable long userId, @PathVariable long friendId) {
        log.info("Requested common friends of users with ids = {}, {}", userId, friendId);
        return userService.getCommonFriends(userId, friendId);
    }
}
