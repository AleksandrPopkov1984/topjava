package ru.javawebinar.topjava.service;

public class Counter {

    public static Integer counter = 1;

    public static synchronized Integer getCounter() {
        return counter++;
    }
}
