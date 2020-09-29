package org.example.validation.analyzers.thread;

import org.example.validation.analyzers.ThreadSafeAnalyzer;
import org.example.validation.annotaions.ThreadSafe;
import org.example.validation.util.MapTarget;
import org.example.validation.util.ThreadTarget;
import org.example.validation.util.ValidationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class ThreadSafeAnalyzerTest {

    ThreadSafeAnalyzer analyzer = new ThreadSafeAnalyzer();
    ValidationResult result = ValidationResult.getInstance();

    /**
     * To check thread safety, we need a
     * separate class with specific fields.
     *
     */
    static class Plug {

        @ThreadSafe(mapTarget = MapTarget.KEYS, threadTarget = ThreadTarget.ALL)
        volatile Map<AtomicBoolean, String> goodMap;

        @ThreadSafe(mapTarget = MapTarget.VALUES)
        Map<Integer, String> badMap;

        @ThreadSafe
        volatile long goodLong;

        @ThreadSafe
        AtomicBoolean goodBoolean;

        @ThreadSafe
        String badStr;

        @ThreadSafe
        int badInt;

        @ThreadSafe(threadTarget = ThreadTarget.ALL)
        volatile Set<Long> set;

        @ThreadSafe
        List<Integer> list;

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

    /**
     * checking volatile long
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate1() throws NoSuchFieldException {

        Plug plug = new Plug();
        plug.goodLong = 10000000L;

        boolean res = analyzer.validate(Plug.class
                .getDeclaredField("goodLong"), plug);

        assertTrue(res);

    }

    /**
     * checking goodMap
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate2() throws NoSuchFieldException {

        Plug plug = new Plug();
        plug.goodMap = new HashMap<>();
        plug.goodBoolean = new AtomicBoolean(true);
        plug.goodMap.put(plug.goodBoolean, plug.badStr);

        boolean res = analyzer.validate(Plug.class
                .getDeclaredField("goodMap"), plug);

        assertTrue(res);

    }

    /**
     * checking badMap
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate3() throws NoSuchFieldException {

        Plug plug = new Plug();
        plug.badMap = new HashMap<>();
        plug.badMap.put(plug.badInt, plug.badStr);

        boolean res = analyzer.validate(Plug.class
                .getDeclaredField("badMap"), plug);

        assertFalse(res);

    }

    /**
     * checking good set
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate4() throws NoSuchFieldException {

        Plug plug = new Plug();
        plug.set = new HashSet<>();
        plug.goodLong = 100000000;
        plug.set.add(plug.goodLong);

        boolean res = analyzer.validate(Plug.class
                .getDeclaredField("set"), plug);

        assertTrue(res);

    }

    /**
     * checking bad list
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate5() throws NoSuchFieldException {

        Plug plug = new Plug();
        plug.list = new ArrayList<>();

        boolean res = analyzer.validate(Plug.class
                .getDeclaredField("list"), plug);

        assertFalse(res);

    }

    /**
     * checking bad null string :)
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateNull() throws NoSuchFieldException {

        Plug plug = new Plug();
        plug.badStr = null;

        boolean res = analyzer.validate(Plug.class
                .getDeclaredField("badStr"), plug);

        assertFalse(res);

    }
}