package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Collection;

public interface FriendshipStorage extends BaseStorage<Friendship> {
    boolean deleteByFriendsId(long id1, long id2);

    Collection<Long> getFriendIds(long id);

    Collection<Long> getCommonFriendIds(long id1, long id2);
}
