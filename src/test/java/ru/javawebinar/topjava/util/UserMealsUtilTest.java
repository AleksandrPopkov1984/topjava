package ru.javawebinar.topjava.util;

import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMealsUtilTest {

    List<UserMeal> meals = Arrays.asList(
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    @Test
    public void filteredByCycles_ShouldReturnCorrectListOfMeals() {
        List<UserMealWithExcess> expected = new ArrayList<>(Arrays.asList(
                new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false),
                new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, true)));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        assertEquals(expected, actual);
    }

    @Test
    public void filteredByStreams_ShouldReturnCorrectListOfMeals() {
        List<UserMealWithExcess> expected = new ArrayList<>(Arrays.asList(
                new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, false),
                new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000, false),
                new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000, true),
                new UserMealWithExcess(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500, true)));

        List<UserMealWithExcess> actual = UserMealsUtil.filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000);

        assertEquals(expected, actual);
    }

}