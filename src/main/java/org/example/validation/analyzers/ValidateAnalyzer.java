package org.example.validation.analyzers;


import org.example.validation.Validator;
import org.example.validation.annotaions.Validate;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.ValidationResult;

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
     * @return true if no inconsistencies were found at runtime, false otherwise
     */
    @Override
    public boolean validate(Field field, Object obj) {

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

        } catch (NullPointerException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @Validate");

            return false;

        } catch (Exception e) {
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

        if (array.length == 0) return;

        Validator.validate(array[0]);

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }

}
