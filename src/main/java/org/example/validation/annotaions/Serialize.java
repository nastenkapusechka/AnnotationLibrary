package org.example.validation.annotaions;

import org.example.validation.util.MapTarget;

public @interface Serialize {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
}
