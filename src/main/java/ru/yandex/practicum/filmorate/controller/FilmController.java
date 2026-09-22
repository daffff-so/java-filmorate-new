package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final Map<Integer, Film> films = new HashMap<>();
    private LocalDate startDate = LocalDate.of(1895, 12, 28);

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Ошибка валидации фильма: не указано название");
            throw new ValidationException("У фильма обязательно нужно указать название");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Ошибка валидации фильма: описание не соответствует требованиям");
            throw new ValidationException("Описание должно иметь не более 200 символов");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(startDate)) {
            log.warn("Ошибка валидации фильма: дата релиза не соответствует требованиям");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            log.warn("Ошибка валидации фильма: продолжительность фильма не соответствует требованиям");
            throw new ValidationException("Продолжительность фильма должна быть больше 0");
        }
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        validateFilm(film);

        film.setId(getNextId());

        films.put(film.getId(), film);

        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {

        validateFilm(newFilm);

        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);

            log.info("Информация о фильме обновлена: {}", newFilm);
            return newFilm;
        } else {
            throw new NotFoundException("Такой фильм не найден");
        }
    }
}
