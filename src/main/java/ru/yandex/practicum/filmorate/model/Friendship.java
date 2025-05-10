package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {
    private Long id;
    private Long friend1_id;
    private Long friend2_id;

    public Friendship(Long id, Long friend1_id, Long friend2_id) {
        this.id = id;
        this.friend1_id = friend1_id;
        this.friend2_id = friend2_id;
    }
}
