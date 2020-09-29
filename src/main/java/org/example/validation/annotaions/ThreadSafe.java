package org.example.validation.annotaions;


import org.example.validation.util.MapTarget;
import org.example.validation.util.ThreadTarget;

import java.lang.annotation.*;

/**
 * This annotation checks if the field is thread safe
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ThreadSafe {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
    ThreadTarget threadTarget() default ThreadTarget.ONLY_FIELD;
}
