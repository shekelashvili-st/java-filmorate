package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Slf4j
@Repository
@Qualifier("H2GenreStorage")
public class DbGenreStorage extends BaseDbStorage<Genre> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY id ASC";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO genres(name) " +
            "VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE genres SET name = ? WHERE id = ?";

    public DbGenreStorage(JdbcTemplate jdbcTemplate, RowMapper<Genre> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Genre add(Genre genre) {
        long id = insert(INSERT_QUERY, genre.getName());
        genre.setId(id);
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        update(UPDATE_QUERY, genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public Genre getById(long id) {
        Genre foundGenre = findOne(FIND_BY_ID_QUERY, id);
        if (foundGenre == null) {
            String message = "Genre with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        return foundGenre;
    }

    @Override
    public Collection<Genre> getAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
