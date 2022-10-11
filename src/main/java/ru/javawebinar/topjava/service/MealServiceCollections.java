package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.javawebinar.topjava.service.Counter.*;
import static ru.javawebinar.topjava.util.MealsUtil.*;

public class MealServiceCollections implements MealService {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void deleteMeal(List<Meal> meals, Integer mealId) {
        meals.removeIf(m -> m.getMealId().equals(mealId));
    }

    @Override
    public void addMeal(List<Meal> meals, HttpServletRequest request) {
        Integer mealId = getCounter();
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), formatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(mealId, dateTime, description, calories);
        meals.add(meal);
    }

    @Override
    public void updateMeal(List<Meal> meals, HttpServletRequest request) {
        Integer mealId = Integer.parseInt(request.getParameter("mealId"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), formatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal mealUpdate = new Meal(mealId, dateTime, description, calories);
        int index = meals.indexOf(getMealById(meals, mealId));
        meals.set(index, mealUpdate);
    }

    @Override
    public Meal getMealById(List<Meal> meals, Integer mealId) {
        return meals.stream().filter(m -> m.getMealId().equals(mealId)).findAny().orElse(null);
    }

    @Override
    public List<MealTo> getListOfMealsToDisplay(List<Meal> meals) {
        return filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), CALORIES_PER_DAY);
    }
}
