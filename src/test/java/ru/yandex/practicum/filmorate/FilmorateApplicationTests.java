package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.DbUserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({DbUserStorage.class, UserMapper.class,
        DbFilmStorage.class, FilmMapper.class})
class FilmorateApplicationTests {
    private final DbUserStorage userStorage;

    @Test
    public void testAddFindUserById() {
        User newUser = new User(null,
                "email@mail.ru",
                "login",
                "name",
                LocalDate.of(2000, 1, 1));
        User addedUser = userStorage.add(newUser);
        User foundUser = userStorage.getById(addedUser.getId());

        assertThat(foundUser).hasFieldOrPropertyWithValue("id", addedUser.getId());
    }

    @Test
    public void testFindAllUsers() {
        User newUser = new User(null,
                "email@mail.ru",
                "login",
                "name",
                LocalDate.of(2000, 1, 1));
        User newUser2 = new User(null,
                "email@mail.ru",
                "login2",
                "name",
                LocalDate.of(2000, 1, 1));
        User addedUser1 = userStorage.add(newUser);
        User addedUser2 = userStorage.add(newUser2);
        User foundUser = userStorage.getById(addedUser1.getId());
        User foundUser2 = userStorage.getById(addedUser2.getId());

        assertThat(foundUser).hasFieldOrPropertyWithValue("login", "login");
        assertThat(foundUser2).hasFieldOrPropertyWithValue("login", "login2");
    }

    @Test
    public void testUpdateUserById() {
        User newUser = new User(null,
                "email@mail.ru",
                "login",
                "name",
                LocalDate.of(2000, 1, 1));
        User addedUser = userStorage.add(newUser);
        User updatedUser = new User(addedUser.getId(),
                "email@mail.ru",
                "loginchanged",
                "name",
                LocalDate.of(2000, 1, 1));
        User foundUser = userStorage.update(updatedUser);

        assertThat(foundUser).hasFieldOrPropertyWithValue("login", updatedUser.getLogin());
    }
}
