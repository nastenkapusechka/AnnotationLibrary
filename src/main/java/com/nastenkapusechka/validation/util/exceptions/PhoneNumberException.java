package com.nastenkapusechka.validation.util.exceptions;

import com.nastenkapusechka.validation.annotaions.PhoneNumber;

/**
 * Thrown if the field is not valid annotation @PhoneNumber
 *
 * @since 2.0
 * @see PhoneNumber
 */
public class PhoneNumberException extends Exception {

    public PhoneNumberException(String msg) {

        super(msg);
    }
}
