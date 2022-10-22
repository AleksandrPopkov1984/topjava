package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;

    public static final int MEAL_1_ID = START_SEQ + 3;
    public static final int MEAL_2_ID = START_SEQ + 4;
    public static final int MEAL_3_ID = START_SEQ + 5;
    public static final int MEAL_4_ID = START_SEQ + 6;
    public static final Meal meal1 = new Meal(MEAL_1_ID, LocalDateTime.of(2022, Month.OCTOBER, 20, 8, 0, 0, 0), "Завтрак", 500);
    public static final Meal meal2 = new Meal(MEAL_2_ID, LocalDateTime.of(2022, Month.OCTOBER, 20, 19, 30, 45, 0), "Ужин", 1000);
    public static final Meal meal3 = new Meal(MEAL_3_ID, LocalDateTime.of(2022, Month.OCTOBER, 21, 8, 0, 0, 0), "Завтрак", 500);
    public static final Meal meal4 = new Meal(MEAL_4_ID, LocalDateTime.of(2022, Month.OCTOBER, 21, 19, 30, 45, 0), "Ужин", 1000);

    public static final LocalDate START_DATE = LocalDate.of(2022, Month.OCTOBER, 20);
    public static final LocalDate END_DATE = LocalDate.of(2022, Month.OCTOBER, 21);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2022, Month.OCTOBER, 22, 12, 30, 50), "Ланч", 250);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
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
        assertThat(actual).isEqualTo(expected);
    }
}
