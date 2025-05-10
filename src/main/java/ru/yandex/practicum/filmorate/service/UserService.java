package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("H2UserStorage") UserStorage storage,
                       @Qualifier("H2FriendshipStorage") FriendshipStorage friendshipStorage) {
        this.userStorage = storage;
        this.friendshipStorage = friendshipStorage;
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(long id) {
        return userStorage.getById(id);
    }

    public User create(User user) {
        User newUser = userStorage.add(user);
        log.info("Added new user successfully: {}", newUser);
        return newUser;
    }

    public User update(User updatedUser) {
        User userInStorage = userStorage.update(updatedUser);
        log.info("Updated user with id {} successfully: {}", userInStorage.getId(), userInStorage);
        return userInStorage;
    }

    public void addToFriends(long id1, long id2) {
        friendshipStorage.add(new Friendship(null, id1, id2));
        log.info("Assigned users with ids {} and {} as friends successfully", id1, id2);
    }

    public void removeFromFriends(long id1, long id2) {
        boolean existed = friendshipStorage.deleteByFriendsId(id1, id2);
        log.debug("User {} was friends with {} before removal: {}", id2, id1, existed);
        log.info("Assigned users with ids {} and {} as non-friends successfully", id1, id2);
    }

    public Collection<User> getFriends(long id) {
        Collection<Long> friendIds = friendshipStorage.getFriendIds(id);
        return friendIds.isEmpty() ? List.of() : userStorage.getUserListByIds(friendIds);
    }

    public Collection<User> getCommonFriends(long id1, long id2) {
        Collection<Long> friendIds = friendshipStorage.getCommonFriendIds(id1, id2);
        return friendIds.isEmpty() ? List.of() : userStorage.getUserListByIds(friendIds);
    }
}
