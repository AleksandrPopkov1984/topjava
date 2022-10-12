package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealsService;
import ru.javawebinar.topjava.service.MealsServiceCollections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.*;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealsService service;
    private DateTimeFormatter formatter;

    @Override
    public void init() {
        service = new MealsServiceCollections();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = request.getParameter("action");

        if (null != action && action.equalsIgnoreCase("delete")) {
            log.debug("delete meal");
            int id = Integer.parseInt(request.getParameter("id"));
            service.delete(id);
            List<MealTo> mealsTo = getListOfMealsToDisplay(service.getAllMeals());
            request.setAttribute("meals", mealsTo);
            response.sendRedirect(request.getContextPath() + "/meals");
        } else if (null != action && action.equalsIgnoreCase("edit")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = service.getById(id);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
        } else {
            List<MealTo> mealsTo = getListOfMealsToDisplay(service.getAllMeals());
            request.setAttribute("meals", mealsTo);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), formatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(dateTime, description, calories);

        if (id == null || id.isEmpty()) {
            log.debug("add new meal");
            service.add(meal);
        } else {
            log.debug("update meal");
            meal.setId(Integer.parseInt(id));
            service.update(meal);
        }
        List<MealTo> mealsTo = getListOfMealsToDisplay(service.getAllMeals());
        request.setAttribute("meals", mealsTo);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private List<MealTo> getListOfMealsToDisplay(List<Meal> meals) {
        return filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), CALORIES_PER_DAY);
    }
}
