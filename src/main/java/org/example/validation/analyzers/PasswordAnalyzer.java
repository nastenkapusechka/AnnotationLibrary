package org.example.validation.analyzers;


import org.example.validation.annotaions.Password;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.CustomException;
import org.example.validation.util.ValidationResult;

import java.lang.reflect.Field;

/**
 * Annotation checks whether the password is strong
 * (a password is considered strong if it contains
 *  at least one uppercase letter, at least
 * one digit, the password is at least 8 characters)
 *
 * The password must be a string. It can be any combination of
 * letters, numbers and other characters (ASCII standard)
 *
 * @see Password
 */
@AnnotationName(Password.class)
public class PasswordAnalyzer implements AnnotationAnalyzer{

    ValidationResult result = ValidationResult.getInstance();
    private static int countIndex;

    /**
     *
     * @param field annotated field
     * @param obj the object of the class that this field belongs to
     * @return true if field is valid, otherwise false
     */
    @Override
    public boolean validate(Field field, Object obj) {

        countIndex = 1;

        if (field == null || obj == null) return false;

        field.setAccessible(true);

        Object[] objects;
        int temp = result.getErrors().size();
        String password;
        boolean res;

        try {

            if (field.get(obj) == null) throw new NullPointerException();

            Password annotation = field.getAnnotation(Password.class);

            objects = convert(field, obj, annotation.mapTarget());

            if (objects != null) {

                recursive(objects, field.getName());
                res = temp == result.getErrors().size();

            } else {

                password = (String) field.get(obj);
                res = check(password);

            }

            if (!res) throw new CustomException();


        } catch (CustomException | ClassCastException | NullPointerException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @Password");

            return false;

        }  catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     *
     * @param array an array of objects to be checked recursively
     *              according to the annotation (for example, array, list, set, or map)
     * @param name the name of the field required to enter information
     *             about it and the number of its element in the resulting list in case of failure.
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
     * This method checks if the password is valid (it means strong - quantity of
     * upper letters, lower letters and digits)
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

}
