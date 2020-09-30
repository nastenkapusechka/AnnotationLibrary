package com.nastenkapusechka.validation.analyzers;


import com.nastenkapusechka.validation.annotaions.ThreadSafe;
import com.nastenkapusechka.validation.util.AnnotationName;
import com.nastenkapusechka.validation.util.ThreadTarget;
import com.nastenkapusechka.validation.util.exceptions.ThreadSafeException;

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

    private static int countIndex;
    Field field;

    /**
     *
     * @param f is field
     * @param obj the object of the class that this field belongs to
     * @return true if field is thread-safe, otherwise exception
     * @throws ThreadSafeException if field is non-thread-safe
     * @throws NullPointerException if field is null
     */
    @Override
    public boolean validate(Field f, Object obj) throws ThreadSafeException {

        countIndex = 1;

        if (f == null || obj == null) return false;

        this.field = f;

        field.setAccessible(true);

        String msg = printPlace(field, obj) + "Doesn't match annotation @ThreadSafe";

        try {

            //first of all we check the field, and then
            // we will check elements
            Object object = field.get(obj);

            if (!check(object)) throw new ThreadSafeException(msg);

            ThreadSafe a = field.getAnnotation(ThreadSafe.class);

            if (a.threadTarget() == ThreadTarget.ONLY_FIELD) return true;

            Object[] objects = convert(field, obj, a.mapTarget());

            if (objects != null) {

                recursive(objects, field.getName());

            } else throw new ThreadSafeException(msg);

            if (objects.length == 0) throw new NullPointerException(msg);

        } catch (IllegalAccessException e) {
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
     *             about it and the number of its element in the exception message in case of failure.
     * @throws ThreadSafeException if field isn't thread-safe
     */
    @Override
    public void recursive(Object[] array, String name) throws ThreadSafeException {

        String place = "Field: " + name + " element #" + countIndex + " ";
        String msg = "is not thread-safe";
        boolean res;

        if (array.length == 0) return;

        res = check(array[0]);

        if (!res) throw new ThreadSafeException(place + msg);

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

        String path = o.getClass().getName();
        int fieldModifiers = field.getModifiers();

        return path.contains("concurrent") ||
                Modifier.isSynchronized(fieldModifiers) ||
                Modifier.isVolatile(fieldModifiers);
    }

}
