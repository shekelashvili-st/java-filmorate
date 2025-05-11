package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendshipMapper implements RowMapper<Friendship> {
    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        long friend1Id = rs.getLong("friend1_id");
        long friend2Id = rs.getLong("friend2_id");

        return new Friendship(id, friend1Id, friend2Id);
    }
}
