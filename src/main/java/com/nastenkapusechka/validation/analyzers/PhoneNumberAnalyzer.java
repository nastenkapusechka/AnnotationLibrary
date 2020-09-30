package com.nastenkapusechka.validation.analyzers;


import com.nastenkapusechka.validation.annotaions.PhoneNumber;
import com.nastenkapusechka.validation.util.AnnotationName;
import com.nastenkapusechka.validation.util.exceptions.PhoneNumberException;

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

    private static int countIndex;

    /**
     *
     * @param field annotated field
     * @param obj the object of the class that this field belongs to
     * @return true if field is valid
     * @throws PhoneNumberException if phone is invalid
     * @throws NullPointerException if phone is null
     */
    @Override
    public boolean validate(Field field, Object obj) throws PhoneNumberException {

        countIndex = 1;

        if (field == null || obj == null) return false;

        field.setAccessible(true);

        String number;
        Object[] objects;
        String msg = printPlace(field, obj) + "Doesn't match annotation @PhoneNumber";

        try {

            if (field.get(obj) == null) throw new NullPointerException(msg);

            PhoneNumber annotation = field.getAnnotation(PhoneNumber.class);

            objects = convert(field, obj, annotation.mapTarget());

            if (objects != null) {

                recursive(objects, field.getName());

            } else if (field.get(obj) instanceof String) {

                number = (String) field.get(obj);
                check(number, msg);

            } else if (field.get(obj) instanceof Number) {

                number = String.valueOf(field.get(obj));
                check(number, msg);

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
     * @throws PhoneNumberException if phone is invalid
     * @throws NullPointerException if phone is null
     */
    @Override
    public void recursive(Object[] array, String name) throws PhoneNumberException {

        String place = "Field: " + name + " element #" + countIndex + " ";
        String msg = "is not a phone number";

        if (array.length == 0) return;

        String number;

        if (array[0] instanceof String) {

            number = (String) array[0];
            check(number, place + msg);

        } else if (array[0] instanceof Number) {

            number = String.valueOf(array[0]);
            check(number, place + msg);

        } else if (array[0] == null) throw new NullPointerException(place + msg);

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }


    /**
     *
     * @param number is string with phone number
     * @param msg is message for exception
     * @throws NullPointerException if phone is null
     * @throws PhoneNumberException if phone is invalid
     *
     */
    private void check(String number, String msg) throws PhoneNumberException {

        if (number == null) throw new NullPointerException(msg);

        String pattern = "^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?$";

        if (!number.matches(pattern)) throw new PhoneNumberException(msg);
    }
}
