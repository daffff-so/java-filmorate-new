package ru.yandex.practicum.filmorate.controller;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    /*
shouldRejectUpdateOfUnknownUser() — неизвестный id → NotFoundException. */

    private UserController controller;

    @BeforeEach
    void setUp() {
        controller = new UserController();
    }

    @Test
    void shouldCreateValidUser() {
        User user = new User();
        user.setEmail("darya@mail");
        user.setLogin("darya");
        user.setName("dar");
        user.setBirthday(LocalDate.of(2024, 6, 24));

        User result = controller.create(user);
        assertEquals(1, result.getId());
        assertEquals(1, controller.findAll().size());
    }

    @Test
    void shouldRejectNullEmail() {
        User user = new User();
        user.setEmail(null);
        user.setLogin("darya");
        user.setName("dar");
        user.setBirthday(LocalDate.of(2024, 6, 24));

        assertThrows(
                ValidationException.class,
                () ->controller.create(user)
        );
    }

    @Test
    void shouldRejectBlankEmail() {
        User user = new User();
        user.setEmail("");
        user.setLogin("darya");
        user.setName("dar");
        user.setBirthday(LocalDate.of(2024, 6, 24));

        assertThrows(
                ValidationException.class,
                () ->controller.create(user)
        );
    }

    @Test
    void shouldRejectEmailWithoutAtSign() {
        User user = new User();
        user.setEmail("daryamail");
        user.setLogin("darya");
        user.setName("dar");
        user.setBirthday(LocalDate.of(2024, 6, 24));

        assertThrows(
                ValidationException.class,
                () ->controller.create(user)
        );
    }

    @Test
    void shouldRejectNullLogin() {
        User user = new User();
        user.setEmail("darya@mail");
        user.setLogin(null);
        user.setName("dar");
        user.setBirthday(LocalDate.of(2024, 6, 24));

        assertThrows(
                ValidationException.class,
                () ->controller.create(user)
        );
    }

    @Test
    void shouldRejectBlankLogin() {
        User user = new User();
        user.setEmail("darya@mail");
        user.setLogin("");
        user.setName("dar");
        user.setBirthday(LocalDate.of(2024, 6, 24));

        assertThrows(
                ValidationException.class,
                () ->controller.create(user)
        );
    }

    @Test
    void shouldRejectLoginWithSpaces() {
        User user = new User();
        user.setEmail("darya@mail");
        user.setLogin("dar ya");
        user.setName("dar");
        user.setBirthday(LocalDate.of(2024, 6, 24));

        assertThrows(
                ValidationException.class,
                () ->controller.create(user)
        );
    }

    @Test
    void shouldUseLoginAsNameWhenNameIsBlank() {
        User user = new User();
        user.setEmail("darya@mail");
        user.setLogin("darya");
        user.setName("");
        user.setBirthday(LocalDate.of(2024, 6, 24));

        User result = controller.create(user);
        assertEquals("darya", result.getName());
    }

    @Test
    void shouldUseLoginAsNameWhenNameIsNull() {
        User user = new User();
        user.setEmail("darya@mail");
        user.setLogin("darya");
        user.setName(null);
        user.setBirthday(LocalDate.of(2024, 6, 24));

        User result = controller.create(user);
        assertEquals("darya", result.getName());
    }

    @Test
    void shouldAcceptBirthdayToday() {
        User user = new User();
        user.setEmail("darya@mail");
        user.setLogin("darya");
        user.setName("dar");
        user.setBirthday(LocalDate.now());

        User result = controller.create(user);
        assertEquals(1, result.getId());
        assertEquals(LocalDate.now(), result.getBirthday());
    }

    @Test
    void shouldRejectBirthdayInFuture() {
        User user = new User();
        user.setEmail("darya@mail");
        user.setLogin("darya");
        user.setName("dar");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(
                ValidationException.class,
                () ->controller.create(user)
        );
    }

    @Test
    void shouldUpdateExistingUser() {
        User user = new User();
        user.setEmail("darya@mail");
        user.setLogin("darya");
        user.setName("dar");
        user.setBirthday(LocalDate.of(2024, 6, 24));

        User createdUser = controller.create(user);

        User updatedUser = new User();
        updatedUser.setEmail("sozi@mail");
        updatedUser.setLogin("darya");
        updatedUser.setName("dar");
        updatedUser.setBirthday(LocalDate.of(2024, 6, 24));

        updatedUser.setId(createdUser.getId());

        User result = controller.update(updatedUser);

        assertEquals(1, result.getId());
        assertEquals("sozi@mail", result.getEmail());
    }

    @Test
    void shouldRejectUpdateOfUnknownUser() {
        User user = new User();
        user.setEmail("darya@mail");
        user.setLogin("darya");
        user.setName("dar");
        user.setBirthday(LocalDate.of(2024, 6, 24));

        assertThrows(
                NotFoundException.class,
                () -> controller.update(user)
        );
    }
}
