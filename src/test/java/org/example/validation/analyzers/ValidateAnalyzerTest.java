package org.example.validation.analyzers;

import org.example.validation.annotaions.*;
import org.example.validation.util.MapTarget;
import org.example.validation.util.ValidationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ValidateAnalyzerTest {

    ValidateAnalyzer analyzer = new ValidateAnalyzer();
    ValidationResult result = ValidationResult.getInstance();

    static class Plug {

        @Validate
        Phone phone;

        @Validate
        CardNumber cardNumber;

        @Validate
        Safe safe;

        @Validate
        Password password;

        @Validate
        SomeObj someObj;

        @Validate
        List<Object> list;

        @Validate
        Set<Object> set;

        @Validate(mapTarget = MapTarget.VALUES)
        Map<Integer, Object> map;

        public Plug(Phone phone, CardNumber cardNumber, Safe safe,
                     Password password, SomeObj someObj) {

            this.phone = phone;
            this.cardNumber = cardNumber;
            this.safe = safe;
            this.password = password;
            this.someObj = someObj;

        }

        static class Phone {

            @PhoneNumber
            String number;

            public Phone(String str) {
                this.number = str;
            }
        }

        static class CardNumber {

            @org.example.validation.annotaions.CardNumber
            String number;

            public CardNumber(String s) {

                this.number = s;
            }
        }

        static class Safe {

            @ThreadSafe
            volatile long n;

            public Safe(long n) {
                this.n = n;
            }

        }

        static class Password {

            @org.example.validation.annotaions.Password
            String password;

            public Password(String s) {
                this.password = s;
            }
        }

        static class SomeObj {

            @NotNull
            Object o;

            public SomeObj(Object o) {
                this.o = o;
            }
        }

    }

    @Before
    public void setUp() {
        result.cleanResults();
    }

    @After
    public void tearDown() {

        result.getErrors().forEach(System.out::println);
        System.out.println();

    }

    @Test
    public void validateGood() throws NoSuchFieldException {

        Plug plug = new Plug(new Plug.Phone("+8199-928-5951"),
                new Plug.CardNumber("3558496271573925"),
                new Plug.Safe(1000L), new Plug.Password("qWErtY4321@"),
                new Plug.SomeObj("im alive!"));

        boolean res1 = analyzer.validate(Plug.class.getDeclaredField("phone"), plug);
        boolean res2 = analyzer.validate(Plug.class.getDeclaredField("cardNumber"), plug);
        boolean res3 = analyzer.validate(Plug.class.getDeclaredField("safe"), plug);
        boolean res4 = analyzer.validate(Plug.class.getDeclaredField("password"), plug);
        boolean res5 = analyzer.validate(Plug.class.getDeclaredField("someObj"), plug);

        assertTrue(res1 && res2 && res3 && res4 && res5);
    }

    @Test
    public void validateBad() throws NoSuchFieldException {

        Plug plug = new Plug(new Plug.Phone("5951"),
                new Plug.CardNumber("1558496271573925"),
                new Plug.Safe(1000L), new Plug.Password("qwerty-asd123"),
                new Plug.SomeObj(null));

        boolean res1 = analyzer.validate(Plug.class.getDeclaredField("phone"), plug);
        boolean res2 = analyzer.validate(Plug.class.getDeclaredField("cardNumber"), plug);
        boolean res4 = analyzer.validate(Plug.class.getDeclaredField("password"), plug);
        boolean res5 = analyzer.validate(Plug.class.getDeclaredField("someObj"), plug);

        assertFalse(res1 && res2 && res4 && res5);
    }

    @Test
    public void validateGoodList() throws NoSuchFieldException {

        Plug plug = new Plug(new Plug.Phone("+8199-928-5951"),
                new Plug.CardNumber("3558496271573925"),
                new Plug.Safe(1000L), new Plug.Password("qWErtY4321@"),
                new Plug.SomeObj("i am not null)"));

        plug.list = new ArrayList<>();

        plug.list.add(plug.phone);
        plug.list.add(plug.cardNumber);
        plug.list.add(plug.safe);
        plug.list.add(plug.password);
        plug.list.add(plug.someObj);

        boolean res1 = analyzer.validate(Plug.class.getDeclaredField("list"), plug);

        assertTrue(res1);
    }

    @Test
    public void validateBadSet() throws NoSuchFieldException {

        Plug plug = new Plug(new Plug.Phone("5951"),
                new Plug.CardNumber("1558496271573925"),
                new Plug.Safe(1000L), new Plug.Password("not_good_password"),
                new Plug.SomeObj(null));

        plug.set = new HashSet<>();

        plug.set.add(plug.phone);
        plug.set.add(plug.cardNumber);
        plug.set.add(plug.safe);
        plug.set.add(plug.password);
        plug.set.add(plug.someObj);

        boolean res1 = analyzer.validate(Plug.class.getDeclaredField("set"), plug);

        assertFalse(res1);
    }

    @Test
    public void validateGoodMap() throws NoSuchFieldException {

        Plug plug = new Plug(new Plug.Phone("+8199-928-5951"),
                new Plug.CardNumber("3558496271573925"),
                new Plug.Safe(1000L), new Plug.Password("qWErtY4321@"),
                new Plug.SomeObj("i am not null)"));

        plug.map = new HashMap<>();

        plug.map.put(1, plug.phone);
        plug.map.put(2, plug.cardNumber);
        plug.map.put(3, plug.safe);
        plug.map.put(4, plug.password);
        plug.map.put(5, plug.someObj);

        boolean res1 = analyzer.validate(Plug.class.getDeclaredField("map"), plug);

        assertTrue(res1);
    }
}