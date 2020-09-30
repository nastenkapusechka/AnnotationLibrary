package com.nastenkapusechka.validation.analyzers;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class NotNullAnalyzerTest {

    NotNullAnalyzer analyzer = new NotNullAnalyzer();

    @Test
    public void validateNotNullList() throws NoSuchFieldException {
        PlugClass cls = new PlugClass.Builder()
                .withList(Arrays.asList("it", "is", "not", "null"))
                .build();

        boolean res = analyzer.validate(PlugClass.class
        .getDeclaredField("list"), cls);

        assertTrue(res);
    }

    @Test
    public void validateNotNullObjects() throws NoSuchFieldException {
        PlugClass cls = new PlugClass.Builder()
                .withObjects(1, 2, 3)
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("objects"), cls);

        assertTrue(res);
    }

    @Test
    public void validateNotNullMap() throws NoSuchFieldException {
        PlugClass cls = new PlugClass.Builder()
                .withMap(new HashMap<>())
                .build();
        cls.map.put(1, "str1");
        cls.map.put(2, "str2");

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("map"), cls);

        assertTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void validateNullString() throws NoSuchFieldException {
        PlugClass cls = new PlugClass.Builder()
                .withString(null)
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("string"), cls);
    }

    @Test(expected = NullPointerException.class)
    public void validateNullStrings() throws NoSuchFieldException {
        PlugClass cls = new PlugClass.Builder()
                .withStrings(null, null, "not null")
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("strings"), cls);
    }

    @Test(expected = NullPointerException.class)
    public void validateNullSet() throws NoSuchFieldException {
        PlugClass cls = new PlugClass.Builder()
                .withSet(new HashSet<>(Arrays.asList(null, null)))
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("set"), cls);
    }
}