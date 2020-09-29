package org.example.validation.analyzers;

import org.example.validation.util.MapTarget;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 *
 * This is a common interface for analyzers. Each specific
 * implementation will in its own way check for validity of
 * fields marked with different annotations
 *
 * @see CardNumberAnalyzer
 * @see EmailAnalyzer
 * @see NotNullAnalyzer
 * @see PasswordAnalyzer
 * @see PhoneNumberAnalyzer
 * @see ThreadSafeAnalyzer
 * @see ValidateAnalyzer
 *
 */
public interface AnnotationAnalyzer {
    /**
     *
     * @param field annotated field
     * @param obj the object of the class that this field belongs to
     * @return True, if the field is valid according to the annotation,
     * otherwise false
     *
     * This method checks the validity of a field
     * marked with one of the library annotations.
     *
     */
    boolean validate(Field field, Object obj);

    /**
     *
     * @param array an array of objects to be checked recursively
     *              according to the annotation (for example, array, list, set, or map)
     * @param name the name of the field required to enter information
     *             about it and the number of its element in the resulting list in case of failure.
     *
     * @see org.example.validation.util.ValidationResult
     * Results
     *
     */
    void recursive(Object[] array, String name);

    /**
     *
     * @param field annotated field
     * @param o the object of the class that this field belongs to
     * @param target If the object is a map, this object must be
     *               marked by MapTarget.KEYS or MapTarget.VALUES
     *               (that is, according to the annotation, we must
     *               check either the keys or the values)
     *
     * @see MapTarget
     * @return converted array of objects from collection, list, set or map
     *
     *
     * @see AnnotationAnalyzer#recursive(Object[], String)
     */
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
     * @param field invalid field
     * @param obj the object of the class that this field belongs to
     * @return message about a mismatch of a given field in a given class
     *
     * @see org.example.validation.util.ValidationResult
     */
    default String printPlace(Field field, Object obj) {

        return "Class: " + obj.getClass().getSimpleName() + " field: " +
                field.getName() + " ";

    }
}


