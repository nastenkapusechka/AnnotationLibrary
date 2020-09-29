package org.example.validation.analyzers;

import org.example.validation.annotaions.CardNumber;
import org.example.validation.util.AnnotationName;
import org.example.validation.util.CustomException;
import org.example.validation.util.ValidationResult;

import java.lang.reflect.Field;


/**
 * This analyzer checks the validity of the card
 * (bank, insurance policy, it doesn't matter) using
 * an algorithm Luna
 * @see CardNumberAnalyzer#algorithmLuna(int[])
 */

@AnnotationName(CardNumber.class)
public class CardNumberAnalyzer implements AnnotationAnalyzer{

    ValidationResult result = ValidationResult.getInstance();
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
     * @param field - class field
     * @param obj - an object that will give us access to the field value
     *
     * @see AnnotationAnalyzer#validate(Field, Object)
     */
    @Override
    public boolean validate(Field field, Object obj) {

        countIndex = 1;

        if (field == null || obj == null) return false;

        field.setAccessible(true);

        String cardNumber;
        boolean res;
        int temp = result.getErrors().size();

        try {

            if (field.get(obj) == null) throw new NullPointerException();

            CardNumber annotation = field.getDeclaredAnnotation(CardNumber.class);

            Object[] objects = convert(field, obj, annotation.mapTarget());

            if (objects != null) {

                recursive(objects, field.getName());
                res = temp == result.getErrors().size();

            } else if (field.get(obj) instanceof String) {

                cardNumber = (String) field.get(obj);
                res = check(cardNumber);

            } else {

                cardNumber = String.valueOf(field.get(obj));
                res = check(cardNumber);

            }

            if (!res) throw new CustomException();

        } catch (CustomException | ClassCastException | NullPointerException e) {

            result.addError(printPlace(field, obj), "Doesn't match annotation @CardNumber");

            return false;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * @see AnnotationAnalyzer#recursive(Object[], String)
     *
     */
    @Override
    public void recursive(Object[] array, String name) {

        String place = "Field: " + name + " element #" + countIndex + " ";
        String msg = "is not card number";
        boolean res = false;

        if (array.length == 0) return;

        String cardNumber;

        if (array[0] instanceof String) {

            cardNumber = (String) array[0];
            res = check(cardNumber);

        } else if (array[0] instanceof Number) {

            cardNumber = String.valueOf(array[0]);
            res = check(cardNumber);

        }

        if (!res) result.addError(place, msg);

        countIndex++;

        Object[] temp = new Object[array.length - 1];
        System.arraycopy(array, 1, temp, 0, temp.length);

        recursive(temp, name);

    }

    /**
     *
     * @param cardNumber the string to be tested
     * @return true, if the string passed the check, false otherwise
     */
    private boolean check(String cardNumber) {

        if (cardNumber == null) return false;

        //remove all unnecessary
        String[] digits = cardNumber.trim()
                .replaceAll(" ", "")
                .replaceAll("\\.", "")
                .replaceAll(",", "")
                .replaceAll("_", "")
                .replaceAll("-", "")
                .split("");

        //No card numbers shorter than 13 digits or longer than 19
        if (digits.length < 13 || digits.length > 19) return false;

        //convert to numbers
        int[] numbers = new int[digits.length];
        for (int i = 0; i < digits.length; i++) {
            numbers[i] = Integer.parseInt(digits[i]);
        }

        return algorithmLuna(numbers);
    }

}
