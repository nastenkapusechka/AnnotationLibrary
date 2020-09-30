package com.nastenkapusechka.validation.util;

import com.nastenkapusechka.validation.annotaions.ThreadSafe;

/**
 *this enum is for annotating collections, maps, or arrays
 * (specifically, an annotation that checks a field for thread
 * safety) to indicate whether the collection itself, an array
 * or a map, or its elements, should be validated, including
 * @see ThreadSafe
 */
public enum ThreadTarget {

    ALL,
    ONLY_FIELD
}
