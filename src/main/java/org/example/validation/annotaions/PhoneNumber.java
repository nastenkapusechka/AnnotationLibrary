package org.example.validation.annotaions;


import org.example.validation.util.MapTarget;

import java.lang.annotation.*;

/**
 * The annotation checks the validity of the number
 * in the international format. The number must be
 * represented by a string
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PhoneNumber {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
}
