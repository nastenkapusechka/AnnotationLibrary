package org.example.validation.analyzers;

import java.lang.reflect.Field;

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


