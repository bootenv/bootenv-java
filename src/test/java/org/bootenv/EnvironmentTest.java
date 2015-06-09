package org.bootenv;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.bootenv.Environment;
import org.bootenv.EnvironmentImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

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
    private static final String VALUE1 = " TrUe";

    private static final String KEY2 = "KEY2";
    private static final String VALUE2 = "fAlSe ";

    private static final String DEFAULT_VALUE = "DEFAULT";

    private static final String KEY3 = "KEY3";

    private static final String KEY4 = "KEY4";

    @Before
    public void setup() throws Exception {
        mockStatic(System.class);

        environment = new EnvironmentImpl();

        expect(System.getenv(KEY1)).andReturn(VALUE1);
        expect(System.getenv(KEY2)).andReturn(VALUE2);
        expect(System.getenv(KEY3)).andReturn(null);
        expect(System.getenv(KEY4)).andReturn("");
    }

    @Test
    public void testHas() throws Exception {
        replayAll();

        assertTrue(environment.hasProperty(KEY1));
        assertTrue(environment.hasProperty(KEY2));
        assertFalse(environment.hasProperty(KEY3));
        assertFalse(environment.hasProperty(KEY4));

        verifyAll();
    }

    @Test
    public void testSupports() throws Exception {
        replayAll();

        assertTrue(environment.supports(KEY1));
        assertFalse(environment.supports(KEY2));
        assertFalse(environment.supports(KEY3));
        assertFalse(environment.supports(KEY4));

        verifyAll();
    }

    @Test
    public void testSupportsWithDefault() throws Exception {
        replayAll();

        assertTrue(environment.supports(KEY1, false));
        assertFalse(environment.supports(KEY2, true));
        assertTrue(environment.supports(KEY3, true));
        assertTrue(environment.supports(KEY4, true));

        verifyAll();
    }

    @Test
    public void testGetOptionalProperty() throws Exception {
        replayAll();

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
        replayAll();

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
    public void testGetPropertyWithDefault() throws Exception {
        replayAll();

        String property = environment.getProperty(KEY1, DEFAULT_VALUE);
        assertEquals(VALUE1, property);

        property = environment.getProperty(KEY2, DEFAULT_VALUE);
        assertEquals(VALUE2, property);

        property = environment.getProperty(KEY3, DEFAULT_VALUE);
        assertEquals(DEFAULT_VALUE, property);

        property = environment.getProperty(KEY4, DEFAULT_VALUE);
        assertEquals(DEFAULT_VALUE, property);

        verifyAll();
    }

    @Test
    public void testGetKeys() throws Exception {
        Map<String, String> properties = new LinkedHashMap<>(2);
        properties.put(KEY1, VALUE1);
        properties.put(KEY2, VALUE2);
        expect(System.getenv()).andReturn(properties);

        replayAll();

        assertTrue(environment.hasProperty(KEY1));
        assertTrue(environment.hasProperty(KEY2));
        assertFalse(environment.hasProperty(KEY3));
        assertFalse(environment.hasProperty(KEY4));

        List<String> keys = Lists.newArrayList(environment.getKeys());
        assertEquals(2, keys.size());
        for (int i = 0; i < 2; i++) {
            assertEquals(i == 0 ? KEY1 : KEY2, keys.get(i));
        }

        verifyAll();
    }

}
