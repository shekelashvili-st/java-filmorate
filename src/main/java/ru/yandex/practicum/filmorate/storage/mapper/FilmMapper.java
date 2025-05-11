package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getTimestamp("releaseDate").toLocalDateTime().toLocalDate();
        int duration = rs.getInt("duration");
        long ratingId = rs.getLong("rating_id");
        Set<Long> genreIds = new HashSet<>();
        if (rs.getLong("genre_id") != 0) {
            genreIds.add(rs.getLong("genre_id"));
        }

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .ratingId(ratingId == 0 ? null : ratingId)
                .genreIds(genreIds)
                .build();
    }
}
