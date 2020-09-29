package org.example.validation.analyzers;

import org.example.validation.annotaions.CardNumber;
import org.example.validation.util.MapTarget;
import org.example.validation.util.ValidationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CardNumberAnalyzerTest {

    CardNumberAnalyzer analyzer = new CardNumberAnalyzer();
    ValidationResult result = ValidationResult.getInstance();

    /**
     * We will need this class for testing maps.
     */
    static class Plug {

        @CardNumber(mapTarget = MapTarget.KEYS)
        Map<String, Integer> goodMap;

        @CardNumber(mapTarget = MapTarget.VALUES)
        Map<Integer, String> badMap;

        public Plug() {

            this.goodMap = new HashMap<>();
            this.badMap = new HashMap<>();

            goodMap.put("4000008449433403", 1);
            badMap.put(1, "1000008449433403");
            badMap.put(2, null);

        }

    }


    @Before
    public void before() {

        result.cleanResults();

    }

    @After
    public void after() {

        result.getErrors().forEach(System.out::println);
        System.out.println();

    }

    /**
     *
     * @throws NoSuchFieldException
     *
     * Here we will check the validity of
     * map with good numbers
     */
    @Test
    public void validateGoodMap() throws NoSuchFieldException {

        boolean result = analyzer.validate(Plug.class
        .getDeclaredField("goodMap"), new Plug());

        assertTrue(result);

    }

    /**
     *
     * @throws NoSuchFieldException
     *
     * Here we will check the validity of
     * map with bad numbers
     */
    @Test
    public void validateBadMap() throws NoSuchFieldException {

        boolean result = analyzer.validate(Plug.class
                .getDeclaredField("badMap"), new Plug());

        assertFalse(result);

    }

    /**
     *
     * @throws NoSuchFieldException
     *
     * Here we will check the validity of
     * the line with a good number.
     */
    @Test
    public void validate1() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder().
                withString("4000008449433403")
                .build();

        Class<?> clazz = PlugClass.class;

        boolean result = analyzer.validate(clazz.getDeclaredField("string"), cls);

        assertTrue(result);

    }

    /**
     *
     * @throws NoSuchFieldException
     *
     *
     * Here we will check the validity of
     * the line with the bad number
     *
     */
    @Test
    public void validate2() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder().
                withString("1000008449433403")
                .build();

        Class<?> clazz = PlugClass.class;

        boolean result = analyzer.validate(clazz.getDeclaredField("string"), cls);

        assertFalse(result);

    }

    /**
     *
     * @throws NoSuchFieldException
     *
     *
     * Here we will check the validity of long
     * array with bad card numbers.
     */
    @Test
    public void validate3() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder().
                withArrayDigits(124, 111, 23423434, 43434323)
                .build();

        Class<?> clazz = PlugClass.class;

        boolean result = analyzer.validate(clazz.getDeclaredField("arrayDigits"), cls);

        assertFalse(result);

    }

    /**
     *
     * @throws NoSuchFieldException
     *
     * Here we will test good String[]
     */

    @Test
    public void validate4() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder()
                .withStrings("3558496271573925", "4751170971974195")
                .build();

        Class<?> clazz = PlugClass.class;

        boolean result = analyzer.validate(clazz.getDeclaredField("strings"), cls);

        assertTrue(result);

    }

    /**
     *
     * @throws NoSuchFieldException
     *
     * Testing List<Integer> of bad numbers
     */
    @Test
    public void validate5() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder()
                .withList(new ArrayList<>(Arrays.asList(123, 111, 321)))
                .build();

        Class<?> clazz = PlugClass.class;

        boolean result = analyzer.validate(clazz.getDeclaredField("list"), cls);

        assertFalse(result);

    }

    /**
     *
     * @throws NoSuchFieldException
     *
     * Testing Set<String> of good numbers
     */
    @Test
    public void validate6() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder()
                .withSet(new HashSet<>(Arrays.asList("3558496271573925",
                        "4751170971974195", "4000008449433403")))
                .build();

        Class<?> clazz = PlugClass.class;

        boolean result = analyzer.validate(clazz.getDeclaredField("set"), cls);

        assertTrue(result);

    }


    /**
     *
     * @throws NoSuchFieldException
     *
     * Testing good Object
     */
    @Test
    public void validate7() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder()
                .withObject("3558496271573925")
                .build();

        Class<?> clazz = PlugClass.class;

        boolean result = analyzer.validate(clazz.getDeclaredField("object"), cls);

        assertTrue(result);

    }

    /**
     *
     * @throws NoSuchFieldException
     *
     * Testing null
     */
    @Test
    public void validate8() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder()
                .withString(null)
                .build();

        Class<?> clazz = PlugClass.class;

        boolean result = analyzer.validate(clazz.getDeclaredField("string"), cls);

        assertFalse(result);

    }
}