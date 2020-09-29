package org.example.validation.analyzers;


import org.example.validation.annotaions.Password;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.CustomException;
import org.example.validation.util.ValidationResult;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * Annotation checks whether the password is strong
 * (a password is considered strong if it contains
 *  at least one uppercase letter, at least
 * one digit, the password is at least 8 characters)
 *
 * The password must be a string. It can be any combination of
 * letters, numbers and other characters (ASCII standard)
 */
@AnnotationName(Password.class)
public class PasswordAnalyzer implements AnnotationAnalyzer{

    ValidationResult result = ValidationResult.getInstance();
    private static int countIndex;

    /**
     *
     * @see AnnotationAnalyzer#validate(Field, Object)
     *
     */
    @Override
    public boolean validate(Field field, Object obj) {

        countIndex = 1;

        if (field == null || obj == null) return false;

        field.setAccessible(true);

        try {

            if (field.get(obj) == null) throw new NullPointerException();

            if (field.get(obj) instanceof Collection<?>) {

                int temp = result.getErrors().size();

                Collection<?> collection = (Collection<?>) field.get(obj);
                Object[] o = collection.toArray();
                recursive(o, field.getName());

                if (temp != result.getErrors().size()) throw new CustomException();

                return true;

            } else if (field.getType().toString().contains("[")) {

                int temp = result.getErrors().size();

                Object[] objects = (Object[]) field.get(obj);
                recursive(objects, field.getName());

                if (temp != result.getErrors().size()) throw new CustomException();

                return true;

            } else if (field.get(obj) instanceof Map) {

                Password annotation = field.getAnnotation(Password.class);
                int temp = result.getErrors().size();

                switch (annotation.mapTarget()) {

                    case KEYS:

                        Map<?, ?> map = (Map<?, ?>) field.get(obj);
                        Object[] o = map.keySet().toArray();
                        recursive(o, field.getName());

                        if (temp != result.getErrors().size()) throw new CustomException();

                        return true;

                    case VALUES:

                        Map<?, ?> map2 = (Map<?, ?>) field.get(obj);
                        Object[] objects = map2.values().toArray();
                        recursive(objects, field.getName());

                        if (temp != result.getErrors().size()) throw new CustomException();

                        return true;
                }
            }

            String password = (String) field.get(obj);

            if (!check(password)) throw new CustomException();


        } catch (CustomException | ClassCastException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @Password");

            return false;

        } catch (NullPointerException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @Password " +
                    "because it is null");

            return false;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     *
     * @see AnnotationAnalyzer#recursive(Object[], String)
     *
     */
    @Override
    public void recursive(Object[] array, String name) {

        String place = "Field: " + name + " element #" + countIndex + " ";
        String msg = "is not good password";
        boolean res = false;

        if (array.length == 0) return;

        String password;

        if (array[0] instanceof String) {

            password = (String) array[0];
            res = check(password);

        }

        if (!res) result.addError(place, msg);

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }

    /**
     *
     * @param password - your password string
     * @return true if password is valid, otherwise false
     *
     * This method checks if the password is valid
     *
     */
    private boolean check(String password) {

        if (password == null) return false;

        //you can add them at your discretion))
        String[] tooEasyPasswords = {"Qwerty123", "Password123", "Йцукен123", "Q1w2e3r4",
                "abc123ABC"};

        String[] letters = password.trim().split("");

        if (letters.length < 8) return false;

        int countUpperLetters = 0;
        int countNumbers = 0;
        int countLowerLetters = 0;

        //we will count letters and digits
        for (String letter : letters) {

            String temp = letter.toUpperCase();
            String temp2 = letter.toLowerCase();

            if (temp.equals(letter) && (!letter.matches("[0-9]"))) countUpperLetters++;

            if (temp2.equals(letter) && (!letter.matches("[0-9]"))) countLowerLetters++;

            if (letter.matches("[0-9]")) countNumbers++;

        }

        if (countNumbers == 0 || countUpperLetters == 0 || countLowerLetters == 0) {

            return false;

        }

        for (String pattern : tooEasyPasswords) {

            if (pattern.contains(password)) return false;

        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {

        return obj instanceof PasswordAnalyzer;
    }

    @Override
    public int hashCode() {

        final int PRIME = 34;
        int result = 1;

        return result * PRIME;

    }
}
