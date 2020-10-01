package com.nastenkapusechka.validation;

import com.nastenkapusechka.validation.annotaions.*;
import com.nastenkapusechka.validation.util.exceptions.PhoneNumberException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidatorTest {

    static class PlugClass {

        @PhoneNumber
        List<String> usersNumbers;

        @Validate
        InnerClass innerClass;

        public PlugClass(List<String> usersNumbers, InnerClass innerClass) {

            this.usersNumbers = usersNumbers;
            this.innerClass = innerClass;

        }

        static class InnerClass {

            @Password
            String password;

            @Email
            String email;

            @NotNull
            String userName;

            public InnerClass(String password, String email, String userName) {

                this.password = password;
                this.email = email;
                this.userName = userName;

            }
        }
    }

    @Test(timeout = 200)
    public void validateGood() throws Exception {

        PlugClass cls = new PlugClass(new ArrayList<>(Arrays.asList("(495)1234567",
                "+7 926 123 45 67")), new PlugClass.InnerClass("n0tQweRty123",
                "good_email@gmail.com", "Vasya"));

        Validator.validate(cls);
    }

    //--- because List<String> phoneNumbers is first ---
    @Test(expected = PhoneNumberException.class)
    public void validateBad() throws Exception {

        PlugClass cls = new PlugClass(new ArrayList<>(Arrays.asList("123123",
                "432432")), new PlugClass.InnerClass("Qwerty123",
                "bad_email@com.", null));

        Validator.validate(cls);
    }
}