package com.nastenkapusechka.validation.analyzers;

import com.nastenkapusechka.validation.util.exceptions.EmailException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class EmailAnalyzerTest {

    EmailAnalyzer analyzer = new EmailAnalyzer();

    @Test
    public void validateGoodString() throws NoSuchFieldException, EmailException {
        PlugClass cls = new PlugClass.Builder()
                .withString("niceandsimple@example.com")
                .build();

        boolean res = analyzer.validate(PlugClass.class
        .getDeclaredField("string"), cls);

        assertTrue(res);
    }

    @Test
    public void validateGoodList() throws NoSuchFieldException, EmailException {
        PlugClass cls = new PlugClass.Builder()
                .withList(new ArrayList<>(Arrays.asList("simplewithsymbol@example.com",
                        "a.little.more.unusual@dept.example.com")))
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("list"), cls);

        assertTrue(res);
    }

    @Test
    public void validateGoodMap() throws NoSuchFieldException, EmailException {
        PlugClass cls = new PlugClass.Builder()
                .withMap(new HashMap<>())
                .build();

        cls.map.put(1, "simplewithsymbol@example.com");
        cls.map.put(2, "a.little.more.unusual@dept.example.com");

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("map"), cls);

        assertTrue(res);
    }

    @Test(expected = EmailException.class)
    public void validateBadStrings() throws NoSuchFieldException, EmailException {
        PlugClass cls = new PlugClass.Builder()
                .withStrings("asd@asd.asd.", "@bad-EMAIL.com")
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("strings"), cls);
    }

    @Test(expected = EmailException.class)
    public void validateBadSet() throws NoSuchFieldException, EmailException {
        PlugClass cls = new PlugClass.Builder()
                .withSet(new HashSet<>(Arrays.asList(1, 2, 3)))
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("set"), cls);
    }

    @Test(expected = NullPointerException.class)
    public void validateNullObjects() throws NoSuchFieldException, EmailException, NullPointerException {
        PlugClass cls = new PlugClass.Builder()
                .withObjects(null, null, null)
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("objects"), cls);
    }
}