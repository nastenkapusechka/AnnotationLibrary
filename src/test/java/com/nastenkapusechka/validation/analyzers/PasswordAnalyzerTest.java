package com.nastenkapusechka.validation.analyzers;

import com.nastenkapusechka.validation.util.exceptions.PasswordException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class PasswordAnalyzerTest {

    PasswordAnalyzer analyzer = new PasswordAnalyzer();

    @Test
    public void validateGoodString() throws NoSuchFieldException, PasswordException {

        PlugClass cls = new PlugClass.Builder()
                .withString("Very_G00d_PASSWOd")
                .build();
        boolean res = analyzer.validate(PlugClass.class
        .getDeclaredField("string"), cls);

        assertTrue(res);
    }

    @Test
    public void validateGoodObjects() throws NoSuchFieldException, PasswordException {

        PlugClass cls = new PlugClass.Builder()
                .withObjects("321ytrewQ", "Very_G00d_PASSWOd")
                .build();
        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("objects"), cls);

        assertTrue(res);
    }

    @Test
    public void validateGoodSet() throws NoSuchFieldException, PasswordException {

        PlugClass cls = new PlugClass.Builder()
                .withSet(new HashSet<>(Arrays.asList("321ytrewQ", "Very_G00d_PASSWOd")))
                .build();
        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("set"), cls);

        assertTrue(res);
    }

    @Test(expected = PasswordException.class)
    public void validateBadList() throws NoSuchFieldException, PasswordException {

        PlugClass cls = new PlugClass.Builder()
                .withList(new ArrayList<>(Arrays.asList("Qwerty123", "34976239476kjhkdfh")))
                .build();
        analyzer.validate(PlugClass.class
                .getDeclaredField("list"), cls);
    }

    @Test(expected = PasswordException.class)
    public void validateBadMap() throws NoSuchFieldException, PasswordException {

        PlugClass cls = new PlugClass.Builder()
                .withMap(new HashMap<>())
                .build();
        cls.map.put(1, "Qwerty123");
        cls.map.put(2, "34976239476kjhkdfh");

        analyzer.validate(PlugClass.class
                .getDeclaredField("map"), cls);
    }

    @Test(expected = NullPointerException.class)
    public void validateNullStrings() throws NoSuchFieldException, PasswordException {

        PlugClass cls = new PlugClass.Builder()
                .withStrings(null, null, null)
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("strings"), cls);
    }
}