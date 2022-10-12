package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsService {

    void delete(Integer id);

    void add(Meal meal);

    void update(Meal meal);

    Meal getById(Integer id);

    List<Meal> getAllMeals();
}
