package com.nastenkapusechka.validation.util.exceptions;

import com.nastenkapusechka.validation.annotaions.Password;

/**
 * Thrown if the field is not valid annotation @Password
 *
 * @since 2.0
 * @see Password
 */

public class PasswordException extends Exception {

    public PasswordException(String msg) {

        super(msg);
    }
}
