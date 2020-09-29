package org.example.validation.analyzers;


import org.example.validation.annotaions.PhoneNumber;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.CustomException;
import org.example.validation.util.ValidationResult;

import java.lang.reflect.Field;

/**
 * The analyzer checks the validity of the
 * phone number in the international format
 * PhoneNumber must be string or a number
 *
 * @see PhoneNumber
 *
 */
@AnnotationName(PhoneNumber.class)
public class PhoneNumberAnalyzer implements AnnotationAnalyzer{

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

        int temp = result.getErrors().size();
        String number;
        Object[] objects;
        boolean res = false;

        try {

            if (field.get(obj) == null) throw new NullPointerException();

            PhoneNumber annotation = field.getAnnotation(PhoneNumber.class);

            objects = convert(field, obj, annotation.mapTarget());

            if (objects != null) {

                recursive(objects, field.getName());
                res = temp == result.getErrors().size();

            } else if (field.get(obj) instanceof String) {

                number = (String) field.get(obj);
                res = check(number);

            } else if (field.get(obj) instanceof Number) {

                number = String.valueOf(field.get(obj));
                res = check(number);

            }

            if (!res) throw new CustomException();

        } catch (CustomException | ClassCastException | NullPointerException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @PhoneNumber");

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
        String msg = "is not a phone number";
        boolean res = false;

        if (array.length == 0) return;

        String number;

        if (array[0] instanceof String) {

            number = (String) array[0];
            res = check(number);

        } else if (array[0] instanceof Number) {

            number = String.valueOf(array[0]);
            res = check(number);

        }

        if (!res) result.addError(place, msg);

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }


    /**
     *
     * @param number is string with phone number
     * @return true if phone is valid otherwise false
     *
     */
    private boolean check(String number) {

        if (number == null) return false;

        String pattern = "^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?$";

        return number.matches(pattern);
    }
}
