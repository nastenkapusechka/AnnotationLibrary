package org.example.validation.analyzers;

import org.example.validation.annotaions.PhoneNumber;
import org.example.validation.util.MapTarget;
import org.example.validation.util.ValidationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PhoneNumberAnalyzerTest {

    PhoneNumberAnalyzer analyzer = new PhoneNumberAnalyzer();
    ValidationResult result = ValidationResult.getInstance();

    /**
     * We will need this class for testing maps.
     */
    static class Plug {

        @PhoneNumber(mapTarget = MapTarget.KEYS)
        Map<String, Integer> goodMap;

        @PhoneNumber(mapTarget = MapTarget.VALUES)
        Map<Integer, String> badMap;

        public Plug() {

            this.goodMap = new HashMap<>();
            this.badMap = new HashMap<>();

            goodMap.put("+4411 9400 8080", 1);
            badMap.put(1, "1231");
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
     * map with good emails
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
     * map with bad emails
     */
    @Test
    public void validateBadMap() throws NoSuchFieldException {

        boolean result = analyzer.validate(Plug.class
                .getDeclaredField("badMap"), new Plug());

        assertFalse(result);

    }


    /**
     * Testing good numbers as String[]
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateGoodNumbers() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withStrings("+4411 9400 8080", "+8199-928-5951")
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("strings"), cls);

        assertTrue(res);
    }


    /**
     * Testing bad number as String
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateBadNumber() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withString("123123123")
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("string"), cls);

        assertFalse(res);
    }


    /**
     * Testing bad numbers as Set<Integer>
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateBadSet() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withSet(new HashSet<>(Arrays.asList(123, 321, 1111111111)))
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("set"), cls);

        assertFalse(res);
    }

    /**
     * Testing good numbers as List<String>
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateGoodList() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withList(new ArrayList<>(Arrays.asList("+4411 9400 8080",
                        "+8199-928-5951")))
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("list"), cls);

        assertTrue(res);
    }

    /**
     * Testing null Object
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateNull() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withObject(null)
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("object"), cls);

        assertFalse(res);
    }
}