package com.nastenkapusechka.validation.analyzers;

import com.nastenkapusechka.validation.util.exceptions.EmailException;
import com.nastenkapusechka.validation.util.exceptions.PhoneNumberException;
import org.junit.Test;


public class ValidateAnalyzerTest {

    ValidateAnalyzer analyzer = new ValidateAnalyzer();

    @Test(expected = EmailException.class)
    public void validate() throws Exception {

        PlugClass cls  = new PlugClass.Builder()
                .withInnerClass("(495)1234567")
                .build();
        analyzer.validate(PlugClass.class
        .getDeclaredField("innerClass"), cls);
    }

    @Test(expected = PhoneNumberException.class)
    public void validate2() throws Exception {

        PlugClass cls  = new PlugClass.Builder()
                .withInnerClass("good_email@mail.ru")
                .build();
        analyzer.validate(PlugClass.class
                .getDeclaredField("innerClass"), cls);
    }

    @Test(expected = NullPointerException.class)
    public void validate3() throws Exception {

        PlugClass cls  = new PlugClass.Builder()
                .withInnerClass(null)
                .build();
        analyzer.validate(PlugClass.class
                .getDeclaredField("innerClass"), cls);
    }
}