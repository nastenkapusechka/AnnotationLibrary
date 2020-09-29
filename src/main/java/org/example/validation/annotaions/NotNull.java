package org.example.validation.annotaions;


import org.example.validation.util.MapTarget;

import java.lang.annotation.*;

/**
 * you can hang this annotation on a class field
 * and it will check that it is not null,
 * otherwise it will output to the console with a
 * reference to this field
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotNull {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
}
