package org.example.validation.util;

/**
 * This enumeration is intended so that you can tell
 * the annotations what you want to check in the map:
 * keys or values. Otherwise, the annotation will not
 * know whether to validate keys or values for it, and
 * will mark the field as non-compliant with the annotation.
 *
 */

public enum MapTarget {

    KEYS,
    VALUES,
    UNKNOWN

}
