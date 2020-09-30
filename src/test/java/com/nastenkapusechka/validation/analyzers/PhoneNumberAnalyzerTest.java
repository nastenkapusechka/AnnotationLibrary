package com.nastenkapusechka.validation.analyzers;

import com.nastenkapusechka.validation.util.exceptions.PhoneNumberException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class PhoneNumberAnalyzerTest {

    PhoneNumberAnalyzer analyzer = new PhoneNumberAnalyzer();

    @Test
    public void validateGoodString() throws NoSuchFieldException, PhoneNumberException {

        PlugClass cls = new PlugClass.Builder()
                .withString("79261234567")
                .build();

        boolean res = analyzer.validate(PlugClass.class
        .getDeclaredField("string"), cls);

        assertTrue(res);
    }

    @Test
    public void validateGoodObjects() throws NoSuchFieldException, PhoneNumberException {

        PlugClass cls = new PlugClass.Builder()
                .withObjects("8-926-123-45-67", "(495)1234567")
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("objects"), cls);

        assertTrue(res);
    }

    @Test
    public void validateGoodMap() throws NoSuchFieldException, PhoneNumberException {

        PlugClass cls = new PlugClass.Builder()
                .withMap(new HashMap<>())
                .build();
        cls.map.put(1, "8-926-123-45-67");
        cls.map.put(2, "(495)1234567");

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("map"), cls);

        assertTrue(res);
    }

    @Test(expected = PhoneNumberException.class)
    public void validateBadSet() throws NoSuchFieldException, PhoneNumberException {

        PlugClass cls = new PlugClass.Builder()
                .withSet(new HashSet<>(Arrays.asList(12342, 6382738)))
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("set"), cls);
    }

    @Test(expected = PhoneNumberException.class)
    public void validateBadList() throws NoSuchFieldException, PhoneNumberException {

        PlugClass cls = new PlugClass.Builder()
                .withList(Arrays.asList("not a number", "111"))
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("list"), cls);
    }

    @Test(expected = NullPointerException.class)
    public void validateNullStrings() throws NoSuchFieldException, PhoneNumberException {

        PlugClass cls = new PlugClass.Builder()
                .withStrings(null, null, null)
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("strings"), cls);
    }
}