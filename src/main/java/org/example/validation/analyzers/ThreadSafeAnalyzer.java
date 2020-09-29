package org.example.validation.analyzers;


import org.example.validation.annotaions.ThreadSafe;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.CustomException;
import org.example.validation.util.ThreadTarget;
import org.example.validation.util.ValidationResult;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

/**
 * The class checks if the annotated field
 * is thread safe
 * @see ThreadSafe
 *
 *
 * BUT! The analyzer checks the field if it belongs
 * to the package java.util.concurrent or if the field is synchronized
 * or volatile
 *
 */
@AnnotationName(ThreadSafe.class)
public class ThreadSafeAnalyzer implements AnnotationAnalyzer{

    ValidationResult result = ValidationResult.getInstance();
    private static int countIndex;
    Field field;

    /**
     *
     * @see AnnotationAnalyzer#validate(Field, Object)
     *
     */
    @Override
    public boolean validate(Field f, Object obj) {

        countIndex = 1;

        if (f == null || obj == null) return false;

        this.field = f;
        field.setAccessible(true);


        try {

            //first of all we check the field, and then
            // we will check elements
            Object object = field.get(obj);

            if (!check(object)) throw new CustomException();

            ThreadSafe a = field.getAnnotation(ThreadSafe.class);

            if (a.target() == ThreadTarget.ONLY_FIELD) return true;

            if (field.get(obj) instanceof Collection<?>) {

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

            } else if (field.get(obj) instanceof Map) {

                ThreadSafe annotation = field.getAnnotation(ThreadSafe.class);
                int temp = result.getErrors().size();

                switch (annotation.mapTarget()) {

                    case KEYS:

                        Map<?, ?> map = (Map<?, ?>) field.get(obj);
                        Object[] o = map.keySet().toArray();
                        recursive(o, field.getName());

                        if (temp != result.getErrors().size()) throw new CustomException();

                        break;

                    case VALUES:

                        Map<?, ?> map2 = (Map<?, ?>) field.get(obj);
                        Object[] objects = map2.values().toArray();
                        recursive(objects, field.getName());

                        if (temp != result.getErrors().size()) throw new CustomException();

                        break;
                }
            }

        } catch (CustomException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @ThreadSafe");

            return false;

        } catch (NullPointerException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @ThreadSafe " +
                    "because it is null");

            return false;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     *
     * @see AnnotationAnalyzer#recursive(Object[], String)
     */
    @Override
    public void recursive(Object[] array, String name) {

        String place = "Field: " + name + " element #" + countIndex + " ";
        String msg = "is not thread-safe";
        boolean res;

        if (array.length == 0) return;

        res = check(array[0]);

        if (!res) result.addError(place, msg);

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }

    /**
     *
     * @param o is object
     * @return true if this object is thread-safe otherwise false
     *
     */
    private boolean check(Object o) {

        if (o == null) return false;

        String path = o.getClass().getName();
        int fieldModifiers = field.getModifiers();

        return path.contains("concurrent") ||
                Modifier.isSynchronized(fieldModifiers) ||
                Modifier.isVolatile(fieldModifiers);
    }

    @Override
    public boolean equals(Object obj) {

        return obj instanceof ThreadSafeAnalyzer;
    }

    @Override
    public int hashCode() {

        final int PRIME = 36;
        int result = 1;

        return result * PRIME;

    }

}
