package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceCollections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.*;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    List<Meal> meals = getListOfMeals();
    MealService service = new MealServiceCollections();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = request.getParameter("action");

        if (action != null && action.equalsIgnoreCase("delete")) {
            log.debug("delete meal");
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            service.deleteMeal(meals, mealId);
            request.setAttribute("meals", service.getListOfMealsToDisplay(meals));
            response.sendRedirect(request.getContextPath() + "/meals");
        } else if (action != null && action.equalsIgnoreCase("edit")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = service.getMealById(meals, mealId);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/editMeal.jsp").forward(request, response);
        } else {
            request.setAttribute("meals", service.getListOfMealsToDisplay(meals));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String mealId = request.getParameter("mealId");

        if (mealId == null || mealId.isEmpty()) {
            log.debug("add new meal");
            service.addMeal(meals, request);
        } else {
            log.debug("update meal");
            service.updateMeal(meals, request);
        }
        request.setAttribute("meals", service.getListOfMealsToDisplay(meals));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
