package org.example.validation.annotaions;


import org.example.validation.util.MapTarget;
import org.example.validation.util.ThreadTarget;

import java.lang.annotation.*;

/**
 * This annotation checks if the field is thread safe
 * <p>If you want to hang annotation on the map, you must
 * definitely specify the target for verification (keys or values)</p>
 * <p>IMPORTANT! Also, if you want to hang this annotation
 *  on a collection, array, or map to check thread safety, you
 *  must specify whether you are only checking the field or
 *  including its elements</p>
 * @see MapTarget
 * @see ThreadTarget
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ThreadSafe {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
    ThreadTarget threadTarget() default ThreadTarget.ONLY_FIELD;
}
