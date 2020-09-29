package org.example.validation.analyzers.notNull;

import org.example.validation.analyzers.NotNullAnalyzer;
import org.example.validation.annotaions.NotNull;
import org.example.validation.util.MapTarget;
import org.example.validation.util.ValidationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class NotNullAnalyzerTest {

    NotNullAnalyzer analyzer = new NotNullAnalyzer();
    ValidationResult result = ValidationResult.getInstance();

    /**
     * We will need this class for testing maps.
     */
    static class Plug {

        @NotNull(mapTarget = MapTarget.KEYS)
        Map<String, Integer> goodMap;

        @NotNull(mapTarget = MapTarget.VALUES)
        Map<Integer, String> badMap;

        public Plug() {

            this.goodMap = new HashMap<>();
            this.badMap = new HashMap<>();

            goodMap.put("4000008449433403", 1);
            badMap.put(1, null);
            badMap.put(2, null);

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


    /**
     *
     * @throws NoSuchFieldException
     *
     * Here we will check the validity of
     * map with non-empty elements
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
     * map with empty elements
     */
    @Test
    public void validateBadMap() throws NoSuchFieldException {

        boolean result = analyzer.validate(Plug.class
                .getDeclaredField("badMap"), new Plug());

        assertFalse(result);

    }


    /**
     * Here we will check for non-empty string
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateNotNull_1() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder()
                .withString("some string")
                .build();

        boolean res = analyzer.validate(PlugClass.class
        .getDeclaredField("string"), cls);

        assertTrue(res);
    }

    /**
     * Here we will check for non-empty Strings[]
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateNotNull_2() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder()
                .withStrings("first", "second", "third")
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("strings"), cls);

        assertTrue(res);
    }

    /**
     * Here we will check for non-empty Set<Integer>
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateNotNull_3() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder()
                .withSet(new HashSet<>(Arrays.asList(1, 2, 3, 4)))
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("set"), cls);

        assertTrue(res);
    }

    /**
     * Here we will check for mixed-empty List<String>
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateMixedNull() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder()
                .withList(new ArrayList<>(Arrays.asList("first", null, "not null")))
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("list"), cls);

        assertFalse(res);
    }

    /**
     * Here we will check for empty Object
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateNull() throws NoSuchFieldException {

        PlugClass cls = new PlugClass.Builder()
                .withObject(null)
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("object"), cls);

        assertFalse(res);
    }
}