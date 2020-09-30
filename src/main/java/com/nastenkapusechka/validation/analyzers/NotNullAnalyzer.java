package com.nastenkapusechka.validation.analyzers;


import com.nastenkapusechka.validation.annotaions.NotNull;
import com.nastenkapusechka.validation.util.AnnotationName;

import java.lang.reflect.Field;

/**
 * Annotation will check if the field is empty or not
 *
 * @see NotNull
 */

@AnnotationName(NotNull.class)
public class NotNullAnalyzer implements AnnotationAnalyzer{

    private static int countIndex;

    /**
     *
     * @param field annotated field
     * @param obj the object of the class that this field belongs to
     * @return true if field is not null, otherwise exception
     * @throws NullPointerException if field contents are null
     */
    @Override
    public boolean validate(Field field, Object obj) {

        countIndex = 1;

        if (obj == null || field == null) return false;

        field.setAccessible(true);

        String msg = printPlace(field, obj) + "Doesn't match annotation @NotNull";

        try {

            if (field.get(obj) == null) throw new NullPointerException(msg);

            NotNull annotation = field.getAnnotation(NotNull.class);

            Object[] objects = convert(field, obj, annotation.mapTarget());

            if (objects != null) recursive(objects, field.getName());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     *
     * @param array an array of objects to be checked recursively
     *              according to the annotation (for example, array, list, set, or map)
     * @param place the name of the field required to enter information
     *              about it and the number of its element in the exception message in case of failure.
     * @throws NullPointerException if field contents are null
     *
     */
    @Override
    public void recursive(Object[] array, String place) {

        if (array.length == 0) return;

        if (array[0] == null) throw new NullPointerException("Field: " +
                place + " element #" + countIndex + " " + "is null");

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, place);

    }
}
