package org.example.validation.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A thread-safe class that will store
 * all error information.
 *
 * The instance can be accessed
 * from anywhere in the library.
 */

public final class ValidationResult {

    private static volatile ValidationResult instance;
    public List<ValidationError> errors;

    private ValidationResult() {
        this.errors = new ArrayList<>();
    }

    /**
     *
     * @return the only instance of this class
     */
    public static ValidationResult getInstance() {

        ValidationResult result = instance;
        if (result != null) return result;

        synchronized (ValidationResult.class) {

            if (instance == null) {
                instance = new ValidationResult();
            }
            return instance;
        }
    }

    /**
     *
     * @param place where non-compliance with the annotation is recorded
     * @param msg non-compliance report
     *
     */
    public void addError(String place, String msg) {

        errors.add(new ValidationError(msg, place, new Date()));

    }

    /**
     *
     * @return a copy of the error list
     */

    public List<String> getErrors() {

        List<String> list = new ArrayList<>();

        errors.forEach(e -> list.add(e.toString()));

        return list;
    }

    /**
     * clean the list
     */
    public void cleanResults() {

        this.errors.clear();

    }

    /**
     * The class that stores the place
     * and message of the inconsistency
     */
    private static class ValidationError{

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        private final String message;
        private final String place;
        private final Date date;

        ValidationError(String message, String place, Date date) {
            this.message = message;
            this.place = place;
            this.date = date;
        }

        @Override
        public String toString() {

            return "Place of inconsistency: " + place +
                    "Message: " + message + " --- " + format.format(date);

        }
    }

}
