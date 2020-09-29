package org.example.validation.annotaions;


import org.example.validation.util.MapTarget;

import java.lang.annotation.*;

/**
 * You can hang this annotation on a class field
 * and it will check that it is not null
 * <p>If you want to hang annotation on the map, you
 * must definitely specify the target for verification (keys or values)</p>
 * @see MapTarget
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotNull {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
}
