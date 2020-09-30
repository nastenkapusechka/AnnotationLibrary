package com.nastenkapusechka.validation.analyzers;

import com.nastenkapusechka.validation.util.exceptions.CardNumberException;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CardNumberAnalyzerTest {

    CardNumberAnalyzer analyzer = new CardNumberAnalyzer();

    @Test
    public void validateGoodString() throws NoSuchFieldException, CardNumberException {
        PlugClass cls = new PlugClass.Builder()
                .withString("4242424242424242")
                .build();

        boolean res = analyzer.validate(PlugClass.class
                        .getDeclaredField("string"), cls);

        assertTrue(res);
    }

    @Test(expected = CardNumberException.class)
    public void validateBadStrings() throws NoSuchFieldException, CardNumberException {
        PlugClass cls = new PlugClass.Builder()
                .withStrings("1242424242424242", "1555555555554444")
                .build();

        analyzer.validate(PlugClass.class.getDeclaredField("strings"), cls);
    }

    @Test
    public void validateGoodList() throws NoSuchFieldException, CardNumberException {
        PlugClass cls = new PlugClass.Builder()
                .withList(new ArrayList<>(Arrays.asList("4242424242424242", "5555555555554444")))
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("list"), cls);

        assertTrue(res);
    }

    @Test(expected = CardNumberException.class)
    public void validateBadSet() throws NoSuchFieldException, CardNumberException {
        PlugClass cls = new PlugClass.Builder()
                .withSet(new HashSet<>(Arrays.asList(1, 2, 3)))
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("set"), cls);
    }

    @Test
    public void validateGoodMap() throws NoSuchFieldException, CardNumberException {
        PlugClass cls = new PlugClass.Builder()
                .withMap(new HashMap<>())
                .build();

        cls.map.put(1, "4242424242424242");
        cls.map.put(2, "5555555555554444");

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("map"), cls);

        assertTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void validateNullObjects() throws NoSuchFieldException, CardNumberException {
        PlugClass cls = new PlugClass.Builder()
                .withObjects((Object[]) null)
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("set"), cls);
    }
}