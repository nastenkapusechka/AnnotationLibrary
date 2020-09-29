package org.example.validation.analyzers;

import org.example.validation.annotaions.Email;
import org.example.validation.util.MapTarget;
import org.example.validation.util.ValidationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class EmailAnalyzerTest {

    ValidationResult result = ValidationResult.getInstance();
    EmailAnalyzer analyzer = new EmailAnalyzer();

    /**
     * We will need this class for testing maps.
     */
    static class Plug {

        @Email(mapTarget = MapTarget.KEYS)
        Map<String, Integer> goodMap;

        @Email(mapTarget = MapTarget.VALUES)
        Map<Integer, String> badMap;

        public Plug() {

            this.goodMap = new HashMap<>();
            this.badMap = new HashMap<>();

            goodMap.put("good_email@gmail.com", 1);
            badMap.put(1, "asd.asd@asd.");
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
     * Testing good email as String
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate1() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withString("myemail123@gmail.com")
                .build();

        boolean result = analyzer.validate(PlugClass.class
                .getDeclaredField("string"), cls);

        assertTrue(result);

    }

    /**
     * Testing good email as String[]
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate2() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withStrings("paula.kahookele@examp2le.com",
                        "hitomi.matsuki@examp1le.com", "some_email@rumbler.com")
                .build();

        boolean result = analyzer.validate(PlugClass.class
                .getDeclaredField("strings"), cls);

        assertTrue(result);

    }

    /**
     * Testing bad email as Set<String>
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate3() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withSet(new HashSet<>(Arrays.asList("asd.asd@asd.", ".@bad.email.com@gmail")))
                .build();

        boolean result = analyzer.validate(PlugClass.class
                .getDeclaredField("set"), cls);

        assertFalse(result);

    }

    /**
     * Testing good email as List<String>
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate4() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withList(new ArrayList<>(Arrays.asList("paula.kahookele@examp2le.com",
                        "hitomi.matsuki@examp1le.com")))
                .build();

        boolean result = analyzer.validate(PlugClass.class
                .getDeclaredField("list"), cls);

        assertTrue(result);

    }

    /**
     * Testing not email as List<Integer>
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate5() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withList(new ArrayList<>(Arrays.asList(123, 111, 321)))
                .build();

        boolean result = analyzer.validate(PlugClass.class
                .getDeclaredField("list"), cls);

        assertFalse(result);

    }

    /**
     * Testing good email as Object
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate6() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withObject("good_email@gmail.com")
                .build();

        boolean result = analyzer.validate(PlugClass.class
                .getDeclaredField("object"), cls);

        assertTrue(result);

    }

    /**
     * Testing not email but number
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate7() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withIntDigits(13)
                .build();

        boolean result = analyzer.validate(PlugClass.class
                .getDeclaredField("intDigits"), cls);

        assertFalse(result);

    }

    /**
     * Testing not email but null
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validate8() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withString(null)
                .build();

        boolean result = analyzer.validate(PlugClass.class
                .getDeclaredField("string"), cls);

        assertFalse(result);

    }
}