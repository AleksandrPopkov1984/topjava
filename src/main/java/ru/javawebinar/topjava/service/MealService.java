package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MealService {

    void deleteMeal(List<Meal> meals, Integer mealId);

    void addMeal(List<Meal> meals, HttpServletRequest request);

    void updateMeal(List<Meal> meals, HttpServletRequest request);

    Meal getMealById(List<Meal> meals, Integer mealId);

    List<MealTo> getListOfMealsToDisplay(List<Meal> meals);
}
