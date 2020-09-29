package org.example.validation.util;


import java.lang.annotation.*;

/**
 * This annotation is for analyzers, so you
 * can find the annotation that the analyzer
 * checks and add to the repository.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationName {
    Class<? extends Annotation> value();
}
