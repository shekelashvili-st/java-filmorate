package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingMapper implements RowMapper<Rating> {
    @Override
    public Rating mapRow(ResultSet rs, int rowNum) throws SQLException {
        long ratingId = rs.getLong("id");
        String name = rs.getString("name");
        return new Rating(ratingId, name);
    }
}
