package org.example.validation.annotaions;

import org.example.validation.util.MapTarget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * An annotation is placed on an object of some
 * class to indicate that not only the object,
 * but also its fields should be checked
 *
 * <p>Annotations also validate the superclass,
 * so your superclass must have a parameterless
 * constructor.</p>
 *
 * <p>If you want to hang annotation on the map,
 * you must definitely specify the target for verification
 * (keys or values)</p>
 * @see MapTarget
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    MapTarget mapTarget() default MapTarget.UNKNOWN;
}
