package org.example.validation.annotaions;


import org.example.validation.util.MapTarget;

import java.lang.annotation.*;


/**
 * Annotation checks the validity of the email
 * Email must be string
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Email {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
}
