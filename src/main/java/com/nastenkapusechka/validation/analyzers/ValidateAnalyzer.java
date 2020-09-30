package com.nastenkapusechka.validation.analyzers;


import com.nastenkapusechka.validation.Validator;
import com.nastenkapusechka.validation.annotaions.Validate;
import com.nastenkapusechka.validation.util.AnnotationName;
import com.nastenkapusechka.validation.util.ValidationResult;

import java.lang.reflect.Field;

/**
 *
 * This is a parser class that will
 * recursively check not only the
 * object itself, but also its fields.
 *
 * @see Validate
 */
@AnnotationName(Validate.class)
public class ValidateAnalyzer implements AnnotationAnalyzer{

    ValidationResult result = ValidationResult.getInstance();

    /**
     *
     * @param field annotated field
     * @param obj the object of the class that this field belongs to
     * @return true if no inconsistencies were found at runtime, false exception
     */
    @Override
    public boolean validate(Field field, Object obj) throws Exception {

        if (field == null || obj == null) return false;

        field.setAccessible(true);

        try {

            if (field.get(obj) == null) throw new NullPointerException();

            int temp = result.getErrors().size();

            Validate annotation = field.getAnnotation(Validate.class);

            Object[] objects = convert(field, obj, annotation.mapTarget());

            if (objects != null) {

                recursive(objects, field.getName());

            } else {

                Object any = field.get(obj);
                Validator.validate(any);

            }

            // If errors are added while the program is running, we notice this.
            return temp == result.getErrors().size();

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
     * @throws Exception if some field in class isn't valid
     */
    @Override
    public void recursive(Object[] array, String name) throws Exception {

        if (array.length == 0) return;

        Validator.validate(array[0]);

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }

}
