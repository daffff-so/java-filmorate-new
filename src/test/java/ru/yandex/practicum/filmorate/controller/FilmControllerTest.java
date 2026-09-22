package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

public class FilmControllerTest {

    private FilmController controller;

    @BeforeEach
    void setUp() {
        controller = new FilmController();
    }

    @Test
    void shouldRejectFilmWithBlankName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("A long adventure");
        film.setReleaseDate(LocalDate.of(2012, 8, 12));
        film.setDuration(108);

        assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
    }

    @Test
    void shouldCreateValidFilm() {
        Film film = new Film();
        film.setName("Adventurous Adventure");
        film.setDescription("A long adventure");
        film.setReleaseDate(LocalDate.of(2012, 8, 12));
        film.setDuration(108);

        Film result = controller.create(film);
        assertEquals(1, result.getId());
        assertEquals(1, controller.findAll().size());
    }

    @Test
    void shouldRejectFilmWithNullName() {
        Film film = new Film();
        film.setName(null);
        film.setDescription("A long adventure");
        film.setReleaseDate(LocalDate.of(2012, 8, 12));
        film.setDuration(108);

        assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
    }

    @Test
    void shouldAcceptDescriptionWith200Characters() {
        Film film = new Film();
        film.setName("Adventurous Adventure");
        film.setDescription("Осень приносит прохладу, золотые листья кружатся в воздухе и тихо падают на землю. " +
                "Утренний туман укутывает город, а чашка горячего чая дарит уют. Природа отдыхает, готовясь к " +
                "долгому зимнему сну.");
        film.setReleaseDate(LocalDate.of(2012, 8, 12));
        film.setDuration(108);

        Film result = controller.create(film);
        assertEquals("Осень приносит прохладу, золотые листья кружатся в воздухе и тихо падают на землю. " +
                "Утренний туман укутывает город, а чашка горячего чая дарит уют. Природа отдыхает, готовясь к " +
                "долгому зимнему сну.", result.getDescription());
    }

    @Test
    void shouldRejectDescriptionLongerThan200Characters() {
        Film film = new Film();
        film.setName("Adventurous Adventure");
        film.setDescription("Осень приносит прохладу, золотые листья кружатся в воздухе и тихо падают на землю. " +
                "Утренний туман укутывает город, а чашка горячего чая дарит уют. Природа отдыхает, готовясь к " +
                "долгому зимнему сну. Вот так вот");
        film.setReleaseDate(LocalDate.of(2012, 8, 12));
        film.setDuration(108);

        assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
    }

    @Test
    void shouldAcceptReleaseDateOnCinemaBirthday() {
        Film film = new Film();
        film.setName("Adventurous Adventure");
        film.setDescription("A long adventure");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(108);

        Film result = controller.create(film);
        assertEquals(LocalDate.of(1895, 12, 28), result.getReleaseDate());
    }

    @Test
    void shouldRejectReleaseDateBeforeCinemaBirthday() {
        Film film = new Film();
        film.setName("Adventurous Adventure");
        film.setDescription("A long adventure");
        film.setReleaseDate(LocalDate.of(1870, 12, 28));
        film.setDuration(108);

        assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
    }

    @Test
    void shouldRejectZeroDuration() {
        Film film = new Film();
        film.setName("Adventurous Adventure");
        film.setDescription("A long adventure");
        film.setReleaseDate(LocalDate.of(2012, 12, 28));
        film.setDuration(0);

        assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
    }

    @Test
    void shouldRejectNegativeDuration() {
        Film film = new Film();
        film.setName("Adventurous Adventure");
        film.setDescription("A long adventure");
        film.setReleaseDate(LocalDate.of(2012, 12, 28));
        film.setDuration(-7);

        assertThrows(
                ValidationException.class,
                () -> controller.create(film)
        );
    }

    @Test
    void shouldUpdateExistingFilm() {
        Film film = new Film();
        film.setName("Adventurous Adventure");
        film.setDescription("A long adventure");
        film.setReleaseDate(LocalDate.of(2012, 12, 28));
        film.setDuration(200);

        Film createdFilm = controller.create(film);

        Film updatedFilm = new Film();
        updatedFilm.setName("Seven wonders");
        updatedFilm.setDescription("A long adventure");
        updatedFilm.setReleaseDate(LocalDate.of(2012, 12, 28));
        updatedFilm.setDuration(200);

        updatedFilm.setId(createdFilm.getId());

        Film result = controller.update(updatedFilm);

        assertEquals(1, result.getId());
        assertEquals("Seven wonders", result.getName());
    }

    @Test
    void shouldRejectUpdateOfUnknownFilm() {
        Film film = new Film();
        film.setName("Adventurous Adventure");
        film.setDescription("A long adventure");
        film.setReleaseDate(LocalDate.of(2012, 12, 28));
        film.setDuration(200);

        Film createdFilm = controller.create(film);

        Film updatedFilm = new Film();
        updatedFilm.setName("Seven wonders");
        updatedFilm.setDescription("A long adventure");
        updatedFilm.setReleaseDate(LocalDate.of(2012, 12, 28));
        updatedFilm.setDuration(200);

        updatedFilm.setId(createdFilm.getId());

        Film result = controller.update(updatedFilm);

        assertEquals(1, result.getId());
        assertEquals("Seven wonders", result.getName());
    }

    @Test
    void shouldRejectUpdateOfUknownFilm() {
        Film film = new Film();
        film.setName("Adventurous Adventure");
        film.setDescription("A long adventure");
        film.setReleaseDate(LocalDate.of(2012, 12, 28));
        film.setDuration(200);

        assertThrows(
                NotFoundException.class,
                () -> controller.update(film)
        );
    }
}
