package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friendship {
    private Long id;
    private Long friend1Id;
    private Long friend2Id;
}
