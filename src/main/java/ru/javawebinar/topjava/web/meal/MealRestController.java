package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.formatter.LocalDateFormatter;
import ru.javawebinar.topjava.util.formatter.LocalTimeFormatter;

import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/meals";

    @Autowired
    private LocalDateFormatter localDateFormatter;

    @Autowired
    private LocalTimeFormatter localTimeFormatter;

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }


    @GetMapping("/filter")
    public List<MealTo> getBetween(@RequestParam(required = false) String startDate,
                                   @RequestParam(required = false) String startTime,
                                   @RequestParam(required = false) String endDate,
                                   @RequestParam(required = false) String endTime) throws ParseException {
        return getBetween(localDateFormatter.parse(startDate, Locale.ENGLISH), localTimeFormatter.parse(startTime, Locale.ENGLISH),
                localDateFormatter.parse(endDate, Locale.ENGLISH), localTimeFormatter.parse(endTime, Locale.ENGLISH));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }
}