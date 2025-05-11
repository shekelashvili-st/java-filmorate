package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public abstract class BaseDbStorage<T> implements BaseStorage<T> {
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> mapper;

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        if (id != null) {
            return id;
        } else {
            String message = "Не удалось сохранить данные";
            log.warn(message);
            throw new InternalServerException(message);
        }
    }

    protected void insertMultiple(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        List<Map<String, Object>> ids = keyHolder.getKeyList();

        if (ids.isEmpty()) {
            String message = "Не удалось сохранить данные";
            log.warn(message);
            throw new InternalServerException(message);
        }
    }

    public boolean delete(String query, Object... params) {
        int rowsDeleted = jdbcTemplate.update(query, params);
        return rowsDeleted > 0;
    }

    protected T findOne(String query, Object... params) {
        try {
            return jdbcTemplate.queryForObject(query, mapper, params);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbcTemplate.query(query, mapper, params);
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbcTemplate.update(query, params);
        if (rowsUpdated == 0) {
            String message = "Не удалось обновить данные";
            log.warn(message);
            throw new InternalServerException(message);
        }
    }
}
