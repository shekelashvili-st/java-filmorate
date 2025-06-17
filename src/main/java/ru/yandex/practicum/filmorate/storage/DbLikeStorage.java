package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.mapper.LikeMapper;

import java.util.Collection;

@Slf4j
@Repository
@Qualifier("H2LikeStorage")
public class DbLikeStorage extends BaseDbStorage<Like> implements LikeStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM likes";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM likes WHERE id = ?";
    private static final String FIND_MOST_LIKED_QUERY = "SELECT film_id FROM likes " +
            "GROUP BY film_id ORDER BY COUNT(user_id) DESC LIMIT ?";
    private static final String INSERT_QUERY = "INSERT INTO likes(film_id, user_id) " +
            "VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE likes SET film_id = ?, user_id = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    @Autowired
    public DbLikeStorage(JdbcTemplate jdbcTemplate, LikeMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public boolean deleteByUserId(long filmId, long userId) {
        return super.delete(DELETE_QUERY, filmId, userId);
    }

    @Override
    public Like add(Like like) {
        long id = insert(INSERT_QUERY, like.getFilmId(), like.getUserId());
        like.setId(id);
        return like;
    }

    @Override
    public Like update(Like like) {
        update(UPDATE_QUERY, like.getFilmId(), like.getUserId(), like.getId());
        return like;
    }

    @Override
    public Like getById(long id) {
        Like foundLike = findOne(FIND_BY_ID_QUERY, id);
        if (foundLike == null) {
            String message = "Like with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        return foundLike;
    }

    @Override
    public Collection<Like> getAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
