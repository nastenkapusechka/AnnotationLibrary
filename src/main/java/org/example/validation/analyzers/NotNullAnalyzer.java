package org.example.validation.analyzers;


import org.example.validation.annotaions.NotNull;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.CustomException;
import org.example.validation.util.ValidationResult;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * Annotation will check if the field is empty or not
 */

@AnnotationName(NotNull.class)
public class NotNullAnalyzer implements AnnotationAnalyzer{

    ValidationResult result = ValidationResult.getInstance();
    private static int countIndex;

    /**
     *
     * @see AnnotationAnalyzer#validate(Field, Object)
     */
    @Override
    public boolean validate(Field field, Object obj) {

        countIndex = 1;

        if (obj == null || field == null) return false;

        try {

            if (field.get(obj) == null) throw new CustomException();

            else if (field.get(obj) instanceof Collection<?>) {

                int temp = result.getErrors().size();

                Collection<?> collection = (Collection<?>) field.get(obj);
                Object[] o = collection.toArray();
                recursive(o, field.getName());

                if (temp != result.getErrors().size()) throw new CustomException();

            } else if (field.getType().toString().contains("[")) {

                int temp = result.getErrors().size();

                Object[] objects = (Object[]) field.get(obj);
                recursive(objects, field.getName());

                if (temp != result.getErrors().size()) throw new CustomException();

                return true;

            } else if (field.get(obj) instanceof Map) {

                NotNull annotation = field.getAnnotation(NotNull.class);
                int temp = result.getErrors().size();

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
     * @see AnnotationAnalyzer#recursive(Object[], String)
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

    @Override
    public boolean equals(Object obj) {

        return obj instanceof NotNullAnalyzer;
    }

    @Override
    public int hashCode() {

        final int PRIME = 33;
        int result = 1;

        return result * PRIME;

    }
}
