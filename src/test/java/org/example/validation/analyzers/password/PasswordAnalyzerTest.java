package org.example.validation.analyzers.password;

import org.example.validation.analyzers.PasswordAnalyzer;
import org.example.validation.annotaions.Password;
import org.example.validation.util.MapTarget;
import org.example.validation.util.ValidationResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class PasswordAnalyzerTest {

    PasswordAnalyzer analyzer = new PasswordAnalyzer();
    ValidationResult result = ValidationResult.getInstance();

    /**
     * We will need this class for testing maps.
     */
    static class Plug {

        @Password(mapTarget = MapTarget.KEYS)
        Map<String, Integer> goodMap;

        @Password(mapTarget = MapTarget.VALUES)
        Map<Integer, String> badMap;

        public Plug() {

            this.goodMap = new HashMap<>();
            this.badMap = new HashMap<>();

            goodMap.put("NotA_qweRty321", 1);
            badMap.put(1, "Qwerty123");
            badMap.put(2, null);
            badMap.put(3, "371133444490t");

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
     * Testing good password as String
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateGoodString() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withString("g00d_Password")
                .build();

        boolean res = analyzer.validate(PlugClass.class
        .getDeclaredField("string"), cls);

        assertTrue(res);
    }

    /**
     * Testing bad password as Strings[]
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateBadStrings() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withStrings("Qwerty123", "1734993849837",
                        "tooSimplePassword")
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("strings"), cls);

        assertFalse(res);
    }

    /**
     * Testing bad password as Set<Integer>
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateBadSet() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withSet(new HashSet<>(Arrays.asList(123123, 983378, 22221111)))
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("set"), cls);

        assertFalse(res);
    }

    /**
     * Testing good passwords as List<String>
     *
     * @throws NoSuchFieldException
     */
    @Test
    public void validateGoodList() throws NoSuchFieldException{

        PlugClass cls = new PlugClass.Builder()
                .withList(new ArrayList<>(Arrays.asList("g00dPassword",
                        "The_same_good123", "Qwerty321asd")))
                .build();

        boolean res = analyzer.validate(PlugClass.class
                .getDeclaredField("list"), cls);

        assertTrue(res);
    }

    /**
     * Testing null password
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