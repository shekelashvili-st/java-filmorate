package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.mapper.FriendshipMapper;

import java.util.Collection;

@Slf4j
@Repository
@Qualifier("H2FriendshipStorage")
public class DbFriendshipStorage extends BaseDbStorage<Friendship> implements FriendshipStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM friendships";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM friendships WHERE id = ?";
    private static final String FIND_FRIENDS_QUERY = "SELECT * FROM friendships WHERE friend1_id = ?";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT f1.id, f1.friend1_id, f1.friend2_id " +
            "FROM friendships AS f1 " +
            "INNER JOIN (SELECT * FROM friendships f2 WHERE friend1_id = ?) AS f2 ON f1.friend2_id = f2.friend2_id " +
            "WHERE f1.friend1_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO friendships(friend1_id, friend2_id) " +
            "VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE friendships SET friend1_id = ?, friend2_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM friendships WHERE friend1_id = ? AND friend2_id = ?";

    @Autowired
    public DbFriendshipStorage(JdbcTemplate jdbcTemplate, FriendshipMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Friendship add(Friendship friendship) {
        long id = insert(INSERT_QUERY, friendship.getFriend1_id(), friendship.getFriend2_id());
        friendship.setId(id);
        return friendship;
    }

    @Override
    public Friendship update(Friendship friendship) {
        update(UPDATE_QUERY, friendship.getFriend1_id(), friendship.getFriend2_id(), friendship.getId());
        return friendship;
    }

    @Override
    public boolean deleteByFriendsId(long id1, long id2) {
        return super.delete(DELETE_QUERY, id1, id2);
    }

    @Override
    public Collection<Long> getFriendIds(long id) {
        Collection<Friendship> friendships = findMany(FIND_FRIENDS_QUERY, id);
        return friendships.stream().map(Friendship::getFriend2_id).toList();
    }

    @Override
    public Collection<Long> getCommonFriendIds(long id1, long id2) {
        Collection<Friendship> friendships = findMany(FIND_COMMON_FRIENDS_QUERY, id1, id2);
        return friendships.stream().map(Friendship::getFriend2_id).toList();
    }

    @Override
    public Friendship getById(long id) {
        Friendship foundFriendship = findOne(FIND_BY_ID_QUERY, id);
        if (foundFriendship == null) {
            String message = "Friendship with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        return foundFriendship;
    }

    @Override
    public Collection<Friendship> getAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
