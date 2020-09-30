package com.nastenkapusechka.validation.annotaions;


import com.nastenkapusechka.validation.util.MapTarget;

import java.lang.annotation.*;

/**
 * The annotation checks the validity of the number
 * in the international format. The number must be
 * represented by a string
 * <p>If you want to hang annotation on the map, you
 * must definitely specify the target for verification (keys or values)</p>
 * @see MapTarget
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PhoneNumber {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
}
