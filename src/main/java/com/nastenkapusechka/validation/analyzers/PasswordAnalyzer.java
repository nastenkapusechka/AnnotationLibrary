package com.nastenkapusechka.validation.analyzers;


import com.nastenkapusechka.validation.annotaions.Password;
import com.nastenkapusechka.validation.util.AnnotationName;
import com.nastenkapusechka.validation.util.exceptions.PasswordException;

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

    private static int countIndex;

    /**
     *
     * @param field annotated field
     * @param obj the object of the class that this field belongs to
     * @return true if field is valid, otherwise exception
     *
     * @throws PasswordException if field doesn't match
     *                  annotation @Password
     * @throws NullPointerException if field contents are null
     */
    @Override
    public boolean validate(Field field, Object obj) throws PasswordException {

        countIndex = 1;

        if (field == null || obj == null) return false;

        field.setAccessible(true);

        Object[] objects;
        String password;
        String msg = printPlace(field, obj) + "Doesn't match annotation @Password";

        try {

            if (field.get(obj) == null) throw new NullPointerException(msg);

            Password annotation = field.getAnnotation(Password.class);

            objects = convert(field, obj, annotation.mapTarget());

            if (objects != null) {

                recursive(objects, field.getName());

            } else {

                password = (String) field.get(obj);
                check(password, msg);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     *
     * @param array an array of objects to be checked recursively
     *              according to the annotation (for example, array, list, set, or map)
     * @param name the name of the field required to enter information
     *             about it and the number of its element in the exception message in case of failure.
     * @throws PasswordException if password is invalid
     */
    @Override
    public void recursive(Object[] array, String name) throws PasswordException, NullPointerException {

        String place = "Field: " + name + " element #" + countIndex + " ";
        String msg = "is not good password";

        if (array.length == 0) return;

        String password;

        if (array[0] instanceof String) {

            password = (String) array[0];
            check(password, place + msg);

        } else if (array[0] == null) throw new NullPointerException(place + msg);

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }

    /**
     *
     * @param password - your password string
     * @param msg is message for exception
     *
     * This method checks if the password is valid (it means strong - quantity of
     * upper letters, lower letters and digits)
     * @throws PasswordException if password is invalid
     */
    private void check(String password, String msg) throws PasswordException {

        if (password == null) throw new NullPointerException(msg);

        //you can add them at your discretion))
        String[] tooEasyPasswords = {"Qwerty123", "Password123", "Йцукен123", "Q1w2e3r4",
                "abc123ABC"};

        String[] letters = password.trim().split("");

        if (letters.length < 8) throw new PasswordException(msg);

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

            throw new PasswordException(msg);

        }

        for (String pattern : tooEasyPasswords) {

            if (pattern.contains(password)) throw new PasswordException(msg);

        }
        //The end:)
    }

}
