package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final Meal breakfast20221020Admin = new Meal(START_SEQ + 3, LocalDateTime.of(2022, Month.OCTOBER, 20, 8, 0, 0, 0), "Завтрак", 500);
    public static final Meal breakfast20221020User = new Meal(START_SEQ + 4, LocalDateTime.of(2022, Month.OCTOBER, 20, 8, 30, 30, 0), "Завтрак", 650);
    public static final Meal lunch20221020Admin = new Meal(START_SEQ + 5, LocalDateTime.of(2022, Month.OCTOBER, 20, 12, 45, 0, 0), "Обед", 1200);
    public static final Meal lunch20221020User = new Meal(START_SEQ + 6, LocalDateTime.of(2022, Month.OCTOBER, 20, 13, 0, 0, 0), "Обед", 1500);
    public static final Meal dinner20221020Admin = new Meal(START_SEQ + 7, LocalDateTime.of(2022, Month.OCTOBER, 20, 19, 30, 45, 0), "Ужин", 1000);
    public static final Meal dinner20221020User = new Meal(START_SEQ + 8, LocalDateTime.of(2022, Month.OCTOBER, 20, 20, 0, 0, 0), "Ужин", 800);
    public static final Meal breakfast20221021User = new Meal(START_SEQ + 9, LocalDateTime.of(2022, Month.OCTOBER, 21, 6, 0, 0, 0), "Завтрак", 500);
    public static final Meal lunch20221021User = new Meal(START_SEQ + 10, LocalDateTime.of(2022, Month.OCTOBER, 21, 12, 10, 0, 0), "Обед", 1000);
    public static final Meal dinner20221021User = new Meal(START_SEQ + 11, LocalDateTime.of(2022, Month.OCTOBER, 21, 18, 19, 25, 0), "Ужин", 400);
    public static final Meal breakfast20221021Admin = new Meal(START_SEQ + 12, LocalDateTime.of(2022, Month.OCTOBER, 21, 7, 12, 37, 0), "Завтрак", 450);
    public static final Meal lunch20221021Admin = new Meal(START_SEQ + 13, LocalDateTime.of(2022, Month.OCTOBER, 21, 13, 34, 0, 0), "Обед", 1100);
    public static final Meal dinner20221021Admin = new Meal(START_SEQ + 14, LocalDateTime.of(2022, Month.OCTOBER, 21, 19, 25, 0, 0), "Ужин", 200);

    public static final LocalDate START_DATE = LocalDate.of(2022, Month.OCTOBER, 20);
    public static final LocalDate END_DATE = LocalDate.of(2022, Month.OCTOBER, 21);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2022, Month.OCTOBER, 22, 12, 30, 50), "Ланч", 250);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(breakfast20221020Admin);
        updated.setDateTime(LocalDateTime.of(2022, Month.OCTOBER, 25, 9, 10, 0, 0));
        updated.setDescription("Второй завтрак");
        updated.setCalories(650);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
