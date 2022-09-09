package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println();

        List<UserMealWithExcess> mealsToByStreams = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToByStreams.forEach(System.out::println);

    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealsTo = new ArrayList<>();
        List<UserMeal> mealsFiltered = new ArrayList<>();
        Map<LocalDate, Integer> mealDate = new HashMap<>();

        for(UserMeal userMeal : meals) {
            if(!mealDate.containsKey(userMeal.getDateTime().toLocalDate())) {
                mealDate.put(userMeal.getDateTime().toLocalDate(), userMeal.getCalories());
            } else {
                mealDate.put(userMeal.getDateTime().toLocalDate(),
                        mealDate.get(userMeal.getDateTime().toLocalDate()) + userMeal.getCalories());
            }
            if(TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealsFiltered.add(userMeal);
            }
        }

        for(UserMeal userMeal : mealsFiltered) {
            mealsTo.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription()
                    , userMeal.getCalories(), mealDate.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
        }

        return mealsTo;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealsTo = new ArrayList<>();
        List<UserMeal> mealsFiltered = new ArrayList<>();

        mealsFiltered = meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime()
                        , startTime, endTime))
                .collect(Collectors.toList());

        Map<LocalDate, Integer> mealDate = meals.stream()
                .collect(Collectors.toMap(meal -> meal.getDateTime().toLocalDate(),
                        meal -> meal.getCalories(), (c1, c2) -> c1 + c2));

        mealsTo = mealsFiltered.stream()
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription()
                        , meal.getCalories(), mealDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());

        return mealsTo;
    }

}
