package org.example.validation.analyzers;


import org.example.validation.annotaions.PhoneNumber;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.CustomException;
import org.example.validation.util.ValidationResult;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * The analyzer checks the validity of the
 * phone number in the international format
 * PhoneNumber must be string or a number
 *
 * @see PhoneNumber
 *
 */
@AnnotationName(PhoneNumber.class)
public class PhoneNumberAnalyzer implements AnnotationAnalyzer{

    ValidationResult result = ValidationResult.getInstance();
    private static int countIndex;

    /**
     *
     * @see AnnotationAnalyzer#validate(Field, Object)
     *
     */
    @Override
    public boolean validate(Field field, Object obj) {

        countIndex = 1;

        if (field == null || obj == null) return false;

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

                PhoneNumber annotation = field.getAnnotation(PhoneNumber.class);
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

            String number;

            if (field.get(obj) instanceof String) {

                number = (String) field.get(obj);
                if (!check(number)) throw new CustomException();

            } else if (field.get(obj) instanceof Number) {

                number = String.valueOf(field.get(obj));
                if (!check(number)) throw new CustomException();

            } else throw new CustomException();


        } catch (CustomException | ClassCastException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @PhoneNumber");

            return false;

        } catch (NullPointerException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @PhoneNumber " +
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

        String place = "Field: " + name + " element #" + countIndex + " ";
        String msg = "is not a phone number";
        boolean res = false;

        if (array.length == 0) return;

        String number;

        if (array[0] instanceof String) {

            number = (String) array[0];
            res = check(number);

        } else if (array[0] instanceof Number) {

            number = String.valueOf(array[0]);
            res = check(number);

        }

        if (!res) result.addError(place, msg);

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }


    /**
     *
     * @param number is string with phone number
     * @return true if phone is valid otherwise false
     *
     */
    private boolean check(String number) {

        if (number == null) return false;

        String pattern = "^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?$";

        return number.matches(pattern);
    }

    @Override
    public boolean equals(Object obj) {

        return obj instanceof PhoneNumberAnalyzer;
    }

    @Override
    public int hashCode() {

        final int PRIME = 35;
        int result = 1;

        return result * PRIME;

    }
}
