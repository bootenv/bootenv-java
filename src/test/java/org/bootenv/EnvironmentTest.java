package org.bootenv;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;
import static org.powermock.api.easymock.PowerMock.*;

/**
 * @author Andrés Amado
 * @since 2015-06-07
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({EnvironmentImpl.class})
public final class EnvironmentTest {

    private Environment environment;

    private static final String KEY1 = "KEY1";
    private static final String KEY2 = "KEY2";
    private static final String KEY3 = "KEY3";
    private static final String KEY4 = "KEY4";
    private static final String KEY5 = "KEY5";
    private static final String KEY6 = "KEY6";

    private static final String VALUE1 = " TrUe";
    private static final String VALUE2 = "fAlSe ";
    private static final String DEFAULT_VALUE = "DEFAULT";
    private static final int DEFAULT_NUMBER = 10;

    @Before
    public void setup() throws Exception {
        mockStatic(System.class);

        environment = new EnvironmentImpl();
    }

    private void replayProperties() {
        expect(System.getenv(KEY1)).andReturn(VALUE1);
        expect(System.getenv(KEY2)).andReturn(VALUE2);
        expect(System.getenv(KEY3)).andReturn(null);
        expect(System.getenv(KEY4)).andReturn("");

        replayAll();
    }

    private void replayNumbers() {
        expect(System.getenv(KEY1)).andReturn("1000000");
        expect(System.getenv(KEY2)).andReturn("2.5");
        expect(System.getenv(KEY3)).andReturn("-3");
        expect(System.getenv(KEY4)).andReturn(null);
        expect(System.getenv(KEY5)).andReturn("abc123");
        expect(System.getenv(KEY6)).andReturn("");

        replayAll();
    }

    @Test
    public void testHas() throws Exception {
        replayProperties();

        assertTrue(environment.hasProperty(KEY1));
        assertTrue(environment.hasProperty(KEY2));
        assertFalse(environment.hasProperty(KEY3));
        assertFalse(environment.hasProperty(KEY4));

        verifyAll();
    }

    @Test
    public void testSupports() throws Exception {
        replayProperties();

        assertTrue(environment.supports(KEY1));
        assertFalse(environment.supports(KEY2));
        assertFalse(environment.supports(KEY3));
        assertFalse(environment.supports(KEY4));

        verifyAll();
    }

    @Test
    public void testSupportsOr() throws Exception {
        replayProperties();

        assertTrue(environment.supportsOr(KEY1, false));
        assertFalse(environment.supportsOr(KEY2, true));
        assertTrue(environment.supportsOr(KEY3, true));
        assertTrue(environment.supportsOr(KEY4, true));

        verifyAll();
    }

    @Test
    public void testGetOptionalProperty() throws Exception {
        replayProperties();

        Optional<String> property = environment.getOptionalProperty(KEY1);
        assertTrue(property.isPresent());
        assertEquals(VALUE1, property.get());

        property = environment.getOptionalProperty(KEY2);
        assertTrue(property.isPresent());
        assertEquals(VALUE2, property.get());

        property = environment.getOptionalProperty(KEY3);
        assertFalse(property.isPresent());

        property = environment.getOptionalProperty(KEY4);
        assertFalse(property.isPresent());

        verifyAll();
    }

    @Test
    public void testGetProperty() throws Exception {
        replayProperties();

        String property = environment.getProperty(KEY1);
        assertEquals(VALUE1, property);

        property = environment.getProperty(KEY2);
        assertEquals(VALUE2, property);

        property = environment.getProperty(KEY3);
        assertNull(property);

        property = environment.getProperty(KEY4);
        assertNull(property);

        verifyAll();
    }

    @Test
    public void testGetPropertyOr() throws Exception {
        replayProperties();

        String property = environment.getPropertyOr(KEY1, DEFAULT_VALUE);
        assertEquals(VALUE1, property);

        property = environment.getPropertyOr(KEY2, DEFAULT_VALUE);
        assertEquals(VALUE2, property);

        property = environment.getPropertyOr(KEY3, DEFAULT_VALUE);
        assertEquals(DEFAULT_VALUE, property);

        property = environment.getPropertyOr(KEY4, DEFAULT_VALUE);
        assertEquals(DEFAULT_VALUE, property);

        verifyAll();
    }

    @Test
    public void testGetOptionalNumber() throws Exception {
        replayNumbers();

        Optional<Number> number = environment.getOptionalNumber(KEY1);
        assertTrue(number.isPresent());
        assertEquals(1000000, number.get().longValue());

        number = environment.getOptionalNumber(KEY2);
        assertTrue(number.isPresent());
        assertEquals(2.5, number.get().doubleValue(), 0);

        number = environment.getOptionalNumber(KEY3);
        assertTrue(number.isPresent());
        assertEquals(-3, number.get().intValue());

        number = environment.getOptionalNumber(KEY4);
        assertFalse(number.isPresent());

        number = environment.getOptionalNumber(KEY5);
        assertFalse(number.isPresent());

        number = environment.getOptionalNumber(KEY6);
        assertFalse(number.isPresent());

        verifyAll();
    }

    @Test
    public void testGetNumber() throws Exception {
        replayNumbers();

        Number number = environment.getNumber(KEY1);
        assertEquals(1000000, number.longValue());

        number = environment.getNumber(KEY2);
        assertEquals(2.5, number.doubleValue(), 0);

        number = environment.getNumber(KEY3);
        assertEquals(-3, number.intValue());

        number = environment.getNumber(KEY4);
        assertNull(number);

        number = environment.getNumber(KEY5);
        assertNull(number);

        number = environment.getNumber(KEY6);
        assertNull(number);

        verifyAll();

    }

    @Test
    public void testGetNumberOr() throws Exception {
        replayNumbers();

        Number number = environment.getNumberOr(KEY1, DEFAULT_NUMBER);
        assertEquals(1000000, number.longValue());

        number = environment.getNumberOr(KEY2, DEFAULT_NUMBER);
        assertEquals(2.5, number.doubleValue(), 0);

        number = environment.getNumberOr(KEY3, DEFAULT_NUMBER);
        assertEquals(-3, number.intValue());

        number = environment.getNumberOr(KEY4, DEFAULT_NUMBER);
        assertEquals(DEFAULT_NUMBER, number.intValue());

        number = environment.getNumberOr(KEY5, DEFAULT_NUMBER);
        assertEquals(DEFAULT_NUMBER, number.intValue());

        number = environment.getNumberOr(KEY6, DEFAULT_NUMBER);
        assertEquals(DEFAULT_NUMBER, number.intValue());

        verifyAll();
    }

    @Test
    public void testGetKeys() throws Exception {
        Map<String, String> properties = new LinkedHashMap<>(2);
        properties.put(KEY1, VALUE1);
        properties.put(KEY2, VALUE2);
        expect(System.getenv()).andReturn(properties);

        replayAll();

        List<String> keys = Lists.newArrayList(environment.getKeys());
        assertEquals(2, keys.size());
        for (int i = 0; i < 2; i++) {
            assertEquals(i == 0 ? KEY1 : KEY2, keys.get(i));
        }

        verifyAll();
    }

}
