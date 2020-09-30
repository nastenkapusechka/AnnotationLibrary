package com.nastenkapusechka.validation.annotaions;


import com.nastenkapusechka.validation.util.MapTarget;

import java.lang.annotation.*;


/**
 * Annotation checks the validity of the email
 * <p>Email must be string</p>
 * <p>If you want to hang annotation on the map, you must
 * definitely specify the target for verification (keys or values)</p>
 * @see MapTarget
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Email {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
}
