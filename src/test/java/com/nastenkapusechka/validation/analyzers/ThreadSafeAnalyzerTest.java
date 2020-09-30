package com.nastenkapusechka.validation.analyzers;

import com.nastenkapusechka.validation.util.exceptions.ThreadSafeException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ThreadSafeAnalyzerTest {

    ThreadSafeAnalyzer analyzer = new ThreadSafeAnalyzer();

    @Test
    public void validateGoodStrings() throws NoSuchFieldException, ThreadSafeException {

        PlugClass cls = new PlugClass.Builder()
                .withStrings("str1", "str2", "str3")
                .build();

        boolean res = analyzer.validate(PlugClass.class
        .getDeclaredField("strings"), cls);

        assertTrue(res);
    }

    @Test(expected = NullPointerException.class)
    public void validateBadList() throws NoSuchFieldException, ThreadSafeException {

        //list is good but empty (ThreadTarget == ALL)
        PlugClass cls = new PlugClass.Builder()
                .withList(new ArrayList<Integer>())
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("list"), cls);
    }

    @Test(expected = ThreadSafeException.class)
    public void validateBadObjects() throws NoSuchFieldException, ThreadSafeException {

        PlugClass cls = new PlugClass.Builder()
                .withObjects("o1", "o2", "o3")
                .build();

        analyzer.validate(PlugClass.class
                .getDeclaredField("objects"), cls);
    }
}