package org.example.validation.annotaions;

import org.example.validation.util.MapTarget;

import java.lang.annotation.*;

/**
 * Annotation checks the validity of card numbers
 * <p>CardNumbers must be string</p>
 * <p>If you want to hang annotation on the map, you must definitely
 * specify the target for verification (keys or values)</p>
 * @see MapTarget
 */


@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CardNumber {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
}
