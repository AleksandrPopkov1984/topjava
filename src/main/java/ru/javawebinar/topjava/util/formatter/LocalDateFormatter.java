package ru.javawebinar.topjava.util.formatter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class LocalDateFormatter implements Formatter<LocalDate> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        if (text.equals("")) {
            return null;
        }
        return LocalDate.parse(text, formatter);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        throw new UnsupportedOperationException();
    }
}
