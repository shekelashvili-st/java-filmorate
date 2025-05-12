package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
@Qualifier("H2FilmStorage")
public class DbFilmStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films AS f " +
            "LEFT OUTER JOIN films_genres AS fg ON f.id = fg.film_id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films AS f " +
            "LEFT OUTER JOIN films_genres AS fg ON f.id = fg.film_id " +
            "WHERE f.id = ?";
    private static final String FIND_LIST_QUERY = "SELECT * FROM films AS f " +
            "LEFT OUTER JOIN films_genres AS fg ON f.id = fg.film_id " +
            "WHERE f.id IN (%s)";
    private static final String FIND_MOST_POPULAR = "SELECT * " +
            "FROM (SELECT f.* " +
            "FROM films AS f " +
            "LEFT OUTER JOIN likes AS l ON f.id = l.film_id " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(l.film_id) DESC " +
            "LIMIT ?) AS fms " +
            "LEFT OUTER JOIN films_genres AS fg ON fms.id = fg.film_id";
    private static final String INSERT_FILM_QUERY = "INSERT INTO films(name, description, releaseDate, duration, rating_id) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO films_genres(film_id, genre_id) " +
            "VALUES (?, ?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE films " +
            "SET name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? WHERE id = ?";
    private static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM films_genres WHERE film_id = ?";

    @Autowired
    public DbFilmStorage(JdbcTemplate jdbcTemplate, FilmMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Film add(Film film) {
        long id = insert(INSERT_FILM_QUERY,
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRatingId());
        film.setId(id);
        batchInsertGenre(id, film.getGenreIds().stream().toList());
        return film;
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_FILM_QUERY,
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRatingId(),
                film.getId());
        // Delete old genres and add new ones
        delete(DELETE_FILM_GENRES_QUERY, film.getId());
        batchInsertGenre(film.getId(), film.getGenreIds().stream().toList());
        return film;
    }

    @Override
    public Film getById(long id) {
        ResultSetExtractor<Film> rse = rs -> {
            Film film = null;
            Collection<Long> genreIds = null;
            int row = 0;
            while (rs.next()) {
                if (film == null) {
                    film = mapper.mapRow(rs, row);
                    genreIds = film.getGenreIds();
                } else {
                    genreIds.add(rs.getLong("genre_id"));
                }
                row++;
            }
            return film;
        };

        Film foundFilm = jdbcTemplate.query(FIND_BY_ID_QUERY, rse, id);
        if (foundFilm == null) {
            String message = "Film with id=" + id + " not found";
            log.warn(message);
            throw new IdNotFoundException(message);
        }
        return foundFilm;
    }

    @Override
    public Collection<Film> getAll() {
        ResultSetExtractor<Collection<Film>> rse = rs -> {
            Collection<Film> films = new ArrayList<>();
            Collection<Long> genreIds = null;
            Long filmId = null;
            Film currentFilm = null;
            int row = 0;
            while (rs.next()) {
                if (currentFilm == null || filmId != rs.getLong("film_id")) {
                    currentFilm = mapper.mapRow(rs, row);
                    filmId = currentFilm.getId();
                    genreIds = currentFilm.getGenreIds();
                    films.add(currentFilm);
                } else {
                    genreIds.add(rs.getLong("genre_id"));
                }
                row++;
            }
            return films;
        };

        return jdbcTemplate.query(FIND_ALL_QUERY, rse);
    }

    @Override
    public List<Film> getFilmListByIds(Collection<Long> ids) {
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        return findMany(FIND_LIST_QUERY.formatted(inSql), ids.toArray());
    }

    @Override
    public List<Film> getMostPopular(long count) {
        ResultSetExtractor<List<Film>> rse = rs -> {
            List<Film> films = new ArrayList<>();
            Collection<Long> genreIds = null;
            Long filmId = null;
            Film currentFilm = null;
            int row = 0;
            while (rs.next()) {
                if (currentFilm == null || filmId != rs.getLong("film_id")) {
                    currentFilm = mapper.mapRow(rs, row);
                    filmId = currentFilm.getId();
                    genreIds = currentFilm.getGenreIds();
                    films.add(currentFilm);
                } else {
                    genreIds.add(rs.getLong("genre_id"));
                }
                row++;
            }
            return films;
        };

        return jdbcTemplate.query(FIND_MOST_POPULAR, rse, count);
    }

    private void batchInsertGenre(Long filmId, List<Long> genreIds) {
        BatchPreparedStatementSetter batchStatementSetter = new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setLong(2, genreIds.get(i));
            }

            public int getBatchSize() {
                return genreIds.size();
            }
        };

        jdbcTemplate.batchUpdate(INSERT_FILM_GENRES_QUERY, batchStatementSetter);
    }
}
