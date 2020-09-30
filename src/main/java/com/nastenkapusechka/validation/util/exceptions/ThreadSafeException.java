package com.nastenkapusechka.validation.util.exceptions;

import com.nastenkapusechka.validation.annotaions.ThreadSafe;

/**
 * Thrown if the field is not valid annotation @ThreadSafe
 *
 * @since 2.0
 * @see ThreadSafe
 */
public class ThreadSafeException extends Exception {

    public ThreadSafeException (String msg) {

        super(msg);
    }
}
