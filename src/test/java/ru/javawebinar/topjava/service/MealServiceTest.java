package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void delete() {
        service.delete(breakfast20221020Admin.getId(), ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(START_SEQ + 3, ADMIN_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(breakfast20221020Admin.getId(), NOT_FOUND));
    }

    @Test
    public void deleteDoesNotBelongToUser() {
        assertThrows(NotFoundException.class, () -> service.delete(breakfast20221020Admin.getId(), USER_ID));
    }

    @Test
    public void get() {
        Meal meal = service.get(lunch20221020User.getId(), USER_ID);
        assertMatch(meal, lunch20221020User);
    }

    @Test
    public void getDoesNotBelongToUser() {
        assertThrows(NotFoundException.class, () -> service.get(lunch20221020User.getId(), ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(lunch20221020Admin.getId(), NOT_FOUND));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> betweenInclusive = service.getBetweenInclusive(START_DATE, END_DATE, USER_ID);
        assertMatch(betweenInclusive, dinner20221021User, lunch20221021User, breakfast20221021User, dinner20221020User,
                lunch20221020User, breakfast20221020User);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, dinner20221021Admin, lunch20221021Admin, breakfast20221021Admin, dinner20221020Admin,
                lunch20221020Admin, breakfast20221020Admin);
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), ADMIN_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, ADMIN_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DuplicateKeyException.class, () ->
                service.create(new Meal(LocalDateTime.of(2022, Month.OCTOBER, 20, 8, 0, 0, 0), "Завтрак", 500), ADMIN_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(breakfast20221020Admin.getId(), ADMIN_ID), getUpdated());
    }

    @Test
    public void updateDoesNotBelongToUser() {
        assertThrows(NotFoundException.class, () -> service.update(breakfast20221020Admin, USER_ID));
    }
}