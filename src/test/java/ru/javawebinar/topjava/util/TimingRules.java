package ru.javawebinar.topjava.util;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TimingRules {

    private static final Logger log = LoggerFactory.getLogger("result");

    private static final StringBuilder results = new StringBuilder();

    public static final Stopwatch STOPWATCH = new Stopwatch() {

        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("%-95s %s %7d", description.getDisplayName(), "passed", TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            log.info(result + " ms\n");
        }

        @Override
        protected void failed(long nanos, Throwable e, Description description) {
            String result = String.format("%-95s %s %7d", description.getDisplayName(), "failed", TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            log.info(result + " ms\n");
        }
    };

    public static final ExternalResource SUMMARY = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            results.setLength(0);
        }

        @Override
        protected void after() {
            log.info("\n" + getDelim(100, '-') + "\nTest + Duration, ms" + "\n" + getDelim(100, '-') + "\n" + results
                    + getDelim(100, '-') + "\n");
        }
    };

    private static String getDelim(int count, char delimeter) {
        String result = "";
        for (int i = 0; i < count; i++) {
            result = result.concat(Character.toString(delimeter));
        }
        return result;
    }

}
