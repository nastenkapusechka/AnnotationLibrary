package com.nastenkapusechka.validation.analyzers;

import com.nastenkapusechka.validation.annotaions.Email;
import com.nastenkapusechka.validation.util.AnnotationName;
import com.nastenkapusechka.validation.util.exceptions.EmailException;

import java.lang.reflect.Field;

/**
 * this analyzer will check email validity.
 * Email should be string
 *
 * @see Email
 */

@AnnotationName(Email.class)
public class EmailAnalyzer implements AnnotationAnalyzer{

    private static int countIndex;

    // regex to validate email addresses
    // according to the official standard RFC 2822
    static String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`" +
            "{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\" +
            "x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:" +
            "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" +
            "|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4]" +
            "[0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e" +
            "-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

    /**
     *
     * @param field annotated field
     * @param obj the object of the class that this field belongs to
     * @return true if the field is valid, otherwise exception
     *
     * @see AnnotationAnalyzer#validate(Field, Object)
     * @throws EmailException if email does not matches the annotation @Email
     * @throws NullPointerException if field contents are null
     */

    @Override
    public boolean validate(Field field, Object obj) throws EmailException {

        countIndex = 1;

        if (field == null || obj == null) return false;

        field.setAccessible(true);

        String email;

        String msg = printPlace(field, obj) + "Doesn't match annotation @Email";

        try {

            if (field.get(obj) == null) throw new NullPointerException();

            Email annotation = field.getAnnotation(Email.class);

            Object[] objects = convert(field, obj, annotation.mapTarget());

            if (objects != null) {

                recursive(objects, field.getName());

            } else {

                email = (String) field.get(obj);

                if (!email.matches(regex)) throw new EmailException(msg);
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
     * @throws EmailException if email does not matches the annotation @Email
     * @throws NullPointerException if field contents are null
     */
    @Override
    public void recursive(Object[] array, String name) throws EmailException {

        String place = "Field: " + name + " element #" + countIndex + " ";
        String msg = "is not email";
        boolean res;

        if (array.length == 0) return;

        String email = null;

        if (array[0] instanceof String) {

            email = (String) array[0];
        }

        if (email == null) throw new NullPointerException(place + msg);

        res = email.matches(regex);

        if (!res) throw new EmailException(place + msg);

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }
}
