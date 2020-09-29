package org.example.validation.analyzers;


import org.example.validation.annotaions.ThreadSafe;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.CustomException;
import org.example.validation.util.ThreadTarget;
import org.example.validation.util.ValidationResult;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
/**
 * The class checks if the annotated field
 * is thread safe
 *
 * <p>BUT! The analyzer checks the field if it belongs
 * to the package java.util.concurrent or if the field is synchronized
 * or volatile</p>
 *
 * @see ThreadSafe
 *
 *
 */
@AnnotationName(ThreadSafe.class)
public class ThreadSafeAnalyzer implements AnnotationAnalyzer{

    ValidationResult result = ValidationResult.getInstance();
    private static int countIndex;
    Field field;

    /**
     *
     * @param f annotated field
     * @param obj the object of the class that this field belongs to
     * @return true if field is valid, otherwise false
     */
    @Override
    public boolean validate(Field f, Object obj) {

        countIndex = 1;

        if (f == null || obj == null) return false;

        this.field = f;
        int temp = result.getErrors().size();
        boolean res;

        field.setAccessible(true);


        try {

            //first of all we check the field, and then
            // we will check elements
            Object object = field.get(obj);

            if (!check(object)) throw new CustomException();

            ThreadSafe a = field.getAnnotation(ThreadSafe.class);

            if (a.threadTarget() == ThreadTarget.ONLY_FIELD) return true;

            Object[] objects = convert(field, obj, a.mapTarget());

            if (objects != null) {

                recursive(objects, field.getName());
                res = temp == result.getErrors().size();

            } else throw new CustomException();

            if (!res) throw new CustomException();

        } catch (CustomException | NullPointerException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @ThreadSafe");

            return false;

        }  catch (Exception e) {
            e.printStackTrace();
            return false;
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

}
