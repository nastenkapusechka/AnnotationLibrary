package org.example.validation.annotaions;

import org.example.validation.util.MapTarget;

import java.lang.annotation.*;

/**
 * Annotation checks the validity of card numbers
 * CardNumbers must be string
 */


@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CardNumber {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
}
