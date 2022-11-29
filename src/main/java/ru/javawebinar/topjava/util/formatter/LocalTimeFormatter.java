package ru.javawebinar.topjava.util.formatter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class LocalTimeFormatter implements Formatter<LocalTime> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        if (text.equals("")) {
            return null;
        }
        return LocalTime.parse(text, formatter);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        throw new UnsupportedOperationException();
    }
}
