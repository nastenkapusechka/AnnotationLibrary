package com.nastenkapusechka.validation;
import com.nastenkapusechka.validation.analyzers.AnnotationAnalyzer;
import com.nastenkapusechka.validation.util.AnnotationRepository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 * Validator.class is the entry point to the library.
 * to use the library you have to call a static method
 * and pass it a class object that will store the annotated fields
 *
 * @author nastenka
 * @version 2.0 release
 *
 * @see Validator#validate(Object)
 */

public final class Validator {

    private final static AnnotationRepository repository = new AnnotationRepository();


    /**
     *
     * @param any is a class object that stores
     *            annotated fields for validation
     *
     * @throws Exception if fields don't match the annotations
     * @see com.nastenkapusechka.validation.annotaions
     * @see com.nastenkapusechka.validation.util.exceptions
     */

    public static synchronized void validate(Object any) throws Exception {

        if (any == null) throw new NullPointerException("Object 'any' is null");

        //take a class
        Class<?> cls = any.getClass();

        //take fields this class
        Field[] fields = cls.getDeclaredFields();

        if (fields.length == 0) throw new NullPointerException("There are no annotated fields in this class");

        for (Field field : fields) {

            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();

            if (annotations.length == 0) continue;

            for (Annotation annotation : annotations) {

                //take analyzer
                AnnotationAnalyzer analyzer = repository.getForAnnotation(annotation.annotationType());

                if (analyzer == null) continue;

                //check the validity of the field
                analyzer.validate(field, any);
            }
        }

    }


}

