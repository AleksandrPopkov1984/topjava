package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


import static ru.javawebinar.topjava.util.MealsUtil.*;

public class MealsServiceCollections implements MealsService {

    public static AtomicInteger counter = new AtomicInteger(8);

    private List<Meal> meals = getListOfMeals();

    @Override
    public void delete(Integer id) {
        meals.removeIf(m -> m.getId().equals(id));
    }

    @Override
    public void add(Meal meal) {
        meal.setId(getCounter());
        meals.add(meal);
    }

    @Override
    public void update(Meal meal) {
        int index = meals.indexOf(getById(meal.getId()));
        meals.set(index, meal);
    }

    @Override
    public Meal getById(Integer id) {
        return meals.stream().filter(m -> m.getId().equals(id)).findAny().orElse(null);
    }

    @Override
    public List<Meal> getAllMeals() {
        return meals;
    }

    private static int getCounter() {
        return counter.getAndIncrement();
    }
}
