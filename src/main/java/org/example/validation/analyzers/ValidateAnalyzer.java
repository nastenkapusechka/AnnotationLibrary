package org.example.validation.analyzers;


import org.example.validation.Validator;
import org.example.validation.annotaions.Validate;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.CustomException;
import org.example.validation.util.ValidationResult;

import java.lang.reflect.Field;
import java.util.*;

/**
 *
 * This is a parser class that will
 * recursively check not only the
 * object itself, but also its fields.
 */
@AnnotationName(Validate.class)
public class ValidateAnalyzer implements AnnotationAnalyzer{

    ValidationResult result = ValidationResult.getInstance();

    /**
     *
     * @param field - class field
     * @param obj - an object that will give us access to the field value
     *
     *
     * The method will check the fields and,
     *            if necessary, add errors to the resulting list
     * @see AnnotationAnalyzer#validate(Field, Object)
     *
     */
    @Override
    public boolean validate(Field field, Object obj) {

        if (field == null || obj == null) return false;

        field.setAccessible(true);

        try {

            if (field.get(obj) == null) throw new NullPointerException();

            int temp = result.getErrors().size();

            if (field.get(obj) instanceof Collection<?>) {

                Collection<?> collection = (Collection<?>) field.get(obj);
                Object[] o = collection.toArray();
                recursive(o, field.getName());

                return temp == result.getErrors().size();

            } else if (field.getType().toString().contains("[")) {

                Object[] objects = (Object[]) field.get(obj);
                recursive(objects, field.getName());

                return temp == result.getErrors().size();

            } else if (field.get(obj) instanceof Map) {

                Validate annotation = field.getAnnotation(Validate.class);

                switch (annotation.mapTarget()) {

                    case KEYS:

                        Map<?, ?> map = (Map<?, ?>) field.get(obj);
                        Object[] o = map.keySet().toArray();
                        recursive(o, field.getName());

                        if (temp != result.getErrors().size()) throw new CustomException();

                        return true;

                    case VALUES:

                        Map<?, ?> map2 = (Map<?, ?>) field.get(obj);
                        Object[] objects = map2.values().toArray();
                        recursive(objects, field.getName());

                        if (temp != result.getErrors().size()) throw new CustomException();

                        return true;
                }
            }

            Object any = field.get(obj);

            Validator.validate(any);

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
     * @see AnnotationAnalyzer#recursive(Object[], String)
     *
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
