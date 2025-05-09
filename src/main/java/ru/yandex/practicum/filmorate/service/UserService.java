package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final BaseStorage<User> storage;

    @Autowired
    public UserService(@Qualifier("H2UserStorage") BaseStorage<User> storage) {
        this.storage = storage;
    }

    public Collection<User> getAll() {
        return storage.getAll();
    }

    public User getById(long id) {
        return storage.getById(id);
    }

    public User create(User user) {
        User newUser = storage.add(user);
        log.info("Added new user successfully: {}", newUser);
        return newUser;
    }

    public User update(User updatedUser) {
        User userInStorage = storage.update(updatedUser);
        log.info("Updated user with id {} successfully: {}", userInStorage.getId(), userInStorage);
        return userInStorage;
    }

    public void addToFriends(long id1, long id2) {
        User user1 = storage.getById(id1);
        User user2 = storage.getById(id2);
        user1.getFriends().add(id2);
        user2.getFriends().add(id1);
        log.info("Assigned users {} and {} as friends successfully", user1, user2);
    }

    public void removeFromFriends(long id1, long id2) {
        User user1 = storage.getById(id1);
        User user2 = storage.getById(id2);
        boolean user2WasUser1Friend = user1.getFriends().remove(id2);
        boolean user1WasUser2Friend = user2.getFriends().remove(id1);
        log.debug("User {} was friends with {} before removal: {}", user2, user1, user2WasUser1Friend);
        log.debug("User {} was friends with {} before removal: {}", user1, user2, user1WasUser2Friend);
        log.info("Assigned users {} and {} as non-friends successfully", user1, user2);
    }

    public Collection<User> getFriends(long id) {
        User user1 = storage.getById(id);
        return user1.getFriends().stream()
                .map(storage::getById)
                .toList();
    }

    public Collection<User> getCommonFriends(long id1, long id2) {
        User user1 = storage.getById(id1);
        User user2 = storage.getById(id2);
        Set<Long> user1Friends = user1.getFriends();
        return user2.getFriends().stream()
                .filter(user1Friends::contains)
                .map(storage::getById)
                .toList();
    }
}
