package org.example.validation.analyzers;


import org.example.validation.annotaions.NotNull;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.CustomException;
import org.example.validation.util.ValidationResult;

import java.lang.reflect.Field;

/**
 * Annotation will check if the field is empty or not
 *
 * @see NotNull
 */

@AnnotationName(NotNull.class)
public class NotNullAnalyzer implements AnnotationAnalyzer{

    ValidationResult result = ValidationResult.getInstance();
    private static int countIndex;

    /**
     *
     * @param field annotated field
     * @param obj the object of the class that this field belongs to
     * @return true if field is not null, otherwise false
     */
    @Override
    public boolean validate(Field field, Object obj) {

        countIndex = 1;

        if (obj == null || field == null) return false;

        field.setAccessible(true);

        try {

            if (field.get(obj) == null) throw new CustomException();

            int temp = result.getErrors().size();

            NotNull annotation = field.getAnnotation(NotNull.class);

            Object[] objects = convert(field, obj, annotation.mapTarget());

            if (objects != null) recursive(objects, field.getName());

            if (temp != result.getErrors().size()) throw new CustomException();

        } catch (CustomException | NullPointerException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @NotNull");

            return false;

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
     *              about it and the number of its element in the resulting list in case of failure.
     */
    @Override
    public void recursive(Object[] array, String place) {

        if (array.length == 0) return;

        if (array[0] == null) result.addError("Field: " +
                place + " element #" + countIndex + " ", "is null");

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, place);

    }
}
