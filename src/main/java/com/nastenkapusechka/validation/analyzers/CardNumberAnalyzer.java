package com.nastenkapusechka.validation.analyzers;

import com.nastenkapusechka.validation.annotaions.CardNumber;
import com.nastenkapusechka.validation.util.AnnotationName;
import com.nastenkapusechka.validation.util.exceptions.CardNumberException;

import java.lang.reflect.Field;


/**
 * This analyzer checks the validity of the card
 * (bank, insurance policy, it doesn't matter) using
 * an algorithm Luna
 * @see CardNumberAnalyzer#algorithmLuna(int[])
 * @see CardNumber
 */

@AnnotationName(CardNumber.class)
public class CardNumberAnalyzer implements AnnotationAnalyzer{

    private static int countIndex;

    /**
     *
     * @param array is an array of digits from the card
     * @return true if such a card exists, otherwise false
     */
    private boolean algorithmLuna(int[] array) {

        for (int i = 0; i < array.length - 1; i = i + 2) {
            array[i] *= 2;
        }

        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > 9) {
                array[i] = array[i] - 9;
            }
        }

        int sum = 0;
        for (int j : array) {
            sum += j;
        }

        return sum % 10 == 0;
    }

    /**
     *
     * @param field annotated field
     * @param obj the object of the class that this field belongs to
     * @return true, if field is valid, otherwise exception
     * @see AnnotationAnalyzer#validate(Field, Object)
     * @throws CardNumberException if cardNumber does not matches the annotation @CardNumber
     * @throws NullPointerException if string is null
     */
    @Override
    public boolean validate(Field field, Object obj) throws CardNumberException {

        countIndex = 1;

        if (field == null || obj == null) throw new NullPointerException();

        String msg = printPlace(field, obj) + "Doesn't match annotation @CardNumber";
        String cardNumber;

        field.setAccessible(true);

        try {

            if (field.get(obj) == null) throw new NullPointerException();

            CardNumber annotation = field.getDeclaredAnnotation(CardNumber.class);

            Object[] objects = convert(field, obj, annotation.mapTarget());

            if (objects != null) {

                recursive(objects, field.getName());

            } else if (field.get(obj) instanceof String) {

                cardNumber = (String) field.get(obj);
                check(cardNumber, msg);

            } else {

                cardNumber = String.valueOf(field.get(obj));
                check(cardNumber, msg);

            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * @param array an array of objects to be checked recursively
     *              according to the annotation (for example, array, list, set, or map)
     * @param name the name of the field required to enter information
     *             about it and the number of its element in the exception message in case of failure.
     *
     * @see AnnotationAnalyzer#recursive(Object[], String)
     * @throws CardNumberException if cardNumber does not matches the annotation @CardNumber
     * @throws NullPointerException if string is null
     */
    @Override
    public void recursive(Object[] array, String name) throws CardNumberException{

        String place = "Field: " + name + " element #" + countIndex;
        String msg = "is not card number";

        if (array.length == 0) return;

        String cardNumber;

        if (array[0] instanceof String) {

            cardNumber = (String) array[0];
            check(cardNumber, place + " " + msg);

        } else if (array[0] instanceof Number) {

            cardNumber = String.valueOf(array[0]);
            check(cardNumber, place + " " + msg);

        }

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }

    /**
     *
     * @param cardNumber a verification string containing the card number
     * @param msg a message for CardNumberException
     * @throws CardNumberException if cardNumber not matches the annotation @CardNumber
     * @throws NullPointerException if string is null
     *
     */
    private void check(String cardNumber, String msg) throws CardNumberException {

        if (cardNumber == null) throw new NullPointerException(msg);

        //remove all unnecessary
        String[] digits = cardNumber.trim()
                .replaceAll(" ", "")
                .replaceAll("\\.", "")
                .replaceAll(",", "")
                .replaceAll("_", "")
                .replaceAll("-", "")
                .split("");

        //No card numbers shorter than 13 digits or longer than 19
        if (digits.length < 13 || digits.length > 19) throw new CardNumberException(msg);

        //convert to numbers
        int[] numbers = new int[digits.length];
        for (int i = 0; i < digits.length; i++) {
            numbers[i] = Integer.parseInt(digits[i]);
        }

        if (!algorithmLuna(numbers)) throw new CardNumberException(msg);
    }

}
