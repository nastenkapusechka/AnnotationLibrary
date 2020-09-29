package org.example.validation.analyzers;

import org.example.validation.util.CustomException;
import org.example.validation.util.MapTarget;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import static org.example.validation.util.MapTarget.KEYS;
import static org.example.validation.util.MapTarget.VALUES;

/**
 * This interface will be implemented by annotation parsers.
 */
public interface AnnotationAnalyzer {

    /**
     *
     * @param field - class field
     * @param obj - an object that will give us access to the field value
     *
     *
     * The method will check the fields and,
     *            if necessary, add errors to the resulting list
     * @see org.example.validation.util.ValidationResult
     *
     * @return true if field is valid, otherwise false
     */
    boolean validate(Field field, Object obj);

    /**
     *
     * @param array is an array of objects to be validated
     * @param place is a field name
     *
     *
     *
     * If the object is a set or list,
     *              we recursively check each
     *              element of that collection.
     *
     */
    void recursive(Object[] array, String place);

    default Object[] convert (Field field, Object o, MapTarget target) {

        try {

            if (field.get(o) instanceof Collection<?>) {

                Collection<?> collection = (Collection<?>) field.get(o);
                return collection.toArray();

            } else if (field.getType().toString().contains("[")) {

                return  (Object[]) field.get(o);

            } else if (field.get(o) instanceof Map) {

                switch (target) {

                    case KEYS:

                        Map<?, ?> map = (Map<?, ?>) field.get(o);
                        return map.keySet().toArray();


                    case VALUES:

                        Map<?, ?> map2 = (Map<?, ?>) field.get(o);
                        return map2.values().toArray();
                }

            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }


        /**
     *
     * @param field the field that failed the check
     * @param obj needed here to get the class of which it is an instance
     *
     * @return line with coordinates
     *
     */
    default String printPlace(Field field, Object obj) {

        return "Class: " + obj.getClass().getSimpleName() + " field: " +
                field.getName() + " ";

    }
}


