package org.example.validation.annotaions;


import org.example.validation.util.MapTarget;

import java.lang.annotation.*;

/**
 * Annotation checks if the password is strong
 * Password must be string
 */

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
}
