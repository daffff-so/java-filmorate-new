package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final Map<Integer, User> users = new HashMap<>();

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.warn("Ошибка валидации пользователя: некорректно введен имейл");
            throw new ValidationException("Имейл должен содержать @");
        }

        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {

            log.warn("Ошибка валидации пользователя: некорректно введен логин");
            throw new ValidationException("Логин не может быть пустым или содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {

            log.warn("Ошибка валидации пользователя: некорректно введена дата Рождения");
            throw new ValidationException("Дата Рождения не должна быть в будущем");
        }
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {

       validateUser(user);

        user.setId(getNextId());
        users.put(user.getId(), user);

        log.info("Добавлен новый пользователь: {}", user);
        return user;
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {

        validateUser(newUser);

        if (users.containsKey(newUser.getId())) {
            users.put(newUser.getId(), newUser);

            log.info("Информация о пользователе обновлена: {}", newUser);
            return newUser;
        } else {
            throw new NotFoundException("Такой пользователь не найден");
        }
    }
}
