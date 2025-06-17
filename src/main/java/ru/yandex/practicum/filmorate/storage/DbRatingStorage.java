package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;

@Slf4j
@Repository
@Qualifier("H2RatingStorage")
public class DbRatingStorage extends BaseDbStorage<Rating> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM ratings ORDER BY id ASC";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ratings WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO ratings(name) " +
            "VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE ratings SET name = ? WHERE id = ?";

    public DbRatingStorage(JdbcTemplate jdbcTemplate, RowMapper<Rating> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Rating add(Rating rating) {
        long id = insert(INSERT_QUERY, rating.getName());
        rating.setId(id);
        return rating;
    }

    @Override
    public Rating update(Rating rating) {
        update(UPDATE_QUERY, rating.getName(), rating.getId());
        return rating;
    }

    @Override
    public Rating getById(long id) {
        Rating foundRating = findOne(FIND_BY_ID_QUERY, id);
        if (foundRating == null) {
            String message = "Rating with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        return foundRating;
    }

    @Override
    public Collection<Rating> getAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
