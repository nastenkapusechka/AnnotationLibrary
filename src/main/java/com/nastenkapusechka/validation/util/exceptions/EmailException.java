package com.nastenkapusechka.validation.util.exceptions;

import com.nastenkapusechka.validation.annotaions.Email;

/**
 * Thrown if the field is not valid annotation @Email
 *
 * @since 2.0
 * @see Email
 */
public class EmailException extends Exception {

    public EmailException(String msg) {

        super(msg);
    }
}
