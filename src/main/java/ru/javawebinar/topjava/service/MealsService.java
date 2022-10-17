package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealsService {

    void delete(int id);

    Meal add(Meal meal);

    Meal update(Meal meal);

    Meal getById(Integer id);

    Collection<Meal> getAll();
}
