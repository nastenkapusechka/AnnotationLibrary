package org.example.validation.analyzers;

import org.example.validation.annotaions.Email;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.CustomException;
import org.example.validation.util.ValidationResult;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * this analyzer will check email validity.
 * Email should be string
 */

@AnnotationName(Email.class)
public class EmailAnalyzer implements AnnotationAnalyzer{

    ValidationResult result = ValidationResult.getInstance();
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
     * @param field - class field
     * @param obj - an object that will give us access to the field value
     *
     * If the field is not validated, the console displays a message
     * @return true if the field is valid, false otherwise
     */

    @Override
    public boolean validate(Field field, Object obj) {

        countIndex = 1;

        if (field == null || obj == null) return false;

        field.setAccessible(true);

        try {

            if (field.get(obj) == null) throw new NullPointerException();

            if (field.get(obj) instanceof Collection<?>) {

                int temp = result.getErrors().size();

                Collection<?> collection = (Collection<?>) field.get(obj);
                Object[] o = collection.toArray();
                recursive(o, field.getName());

                if (temp != result.getErrors().size()) throw new CustomException();

                return true;

            } else if (field.getType().toString().contains("[")) {

                int temp = result.getErrors().size();

                Object[] objects = (Object[]) field.get(obj);
                recursive(objects, field.getName());

                if (temp != result.getErrors().size()) throw new CustomException();

                return true;

            } else if (field.get(obj) instanceof Map) {

                Email annotation = field.getAnnotation(Email.class);
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

            String email = (String) field.get(obj);

            if (!email.matches(regex)) throw new CustomException();


        } catch (CustomException | ClassCastException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @Email");

            return false;

        } catch (NullPointerException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @Email " +
                    "because it is null");

            return false;

        } catch (IllegalAccessException e) {
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

        String place = "Field: " + name + " element #" + countIndex;
        String msg = "is not email";
        boolean res = false;

        if (array.length == 0) return;

        String email;

        if (array[0] instanceof String) {

            email = (String) array[0];
            res = email.matches(regex);

        }

        if (!res) result.addError(place, msg);

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }

    @Override
    public boolean equals(Object obj) {

        return obj instanceof EmailAnalyzer;
    }

    @Override
    public int hashCode() {

        final int PRIME = 32;
        int result = 1;

        return result * PRIME;

    }
}
