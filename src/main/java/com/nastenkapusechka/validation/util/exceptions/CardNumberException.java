package com.nastenkapusechka.validation.util.exceptions;

import com.nastenkapusechka.validation.annotaions.CardNumber;

/**
 *
 * Thrown if the field is not valid annotation @CardNumber
 *
 * @since 2.0
 * @see CardNumber
 */
public class CardNumberException extends Exception {

    public CardNumberException (String msg) {

        super(msg);
    }
}
