package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        long filmId = rs.getLong("film_id");
        long userId = rs.getLong("user_id");

        return new Like(id, filmId, userId);
    }
}
