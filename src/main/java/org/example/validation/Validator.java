package org.example.validation;
import org.example.validation.analyzers.*;
import org.example.validation.util.AnnotationRepository;
import org.example.validation.util.ValidationResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @version 1.0 SNAPSHOT
 *
 * Validator.class is the entry point to the library.
 * to use the library you have to call a static method
 * and pass it a class object that will store the annotated fields
 * @see Validator#validate(Object)
 */

public final class Validator {

    private final static AnnotationRepository repository = new AnnotationRepository();

    static ValidationResult result = ValidationResult.getInstance();


    /**
     *
     * @param any is a class object that stores
     *            annotated fields for validation
     * @return a list of errors, if any
     */

    public static synchronized List<String> validate(Object any) {

        if (any == null) return result.getErrors();

        //take a class
        Class<?> cls = any.getClass();

        //take fields this class
        Field[] fields = cls.getDeclaredFields();

        if (fields.length == 0) return result.getErrors();

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

        return result.getErrors();
    }


}

