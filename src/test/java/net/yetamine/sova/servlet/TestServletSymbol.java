/*
 * Copyright 2016 Yetamine
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.yetamine.sova.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.yetamine.sova.AdaptationProvider;
import net.yetamine.sova.AdaptationResult;
import net.yetamine.sova.Downcasting;

/**
 * Tests {@link ServletSymbol}.
 */
public final class TestServletSymbol {

    /** Value to be present under the key {@code "integer"}. */
    private static final Integer INTEGER_VALUE = Integer.valueOf(1024);
    /** Value to be present under the key {@code "string"}. */
    private static final String STRING_VALUE = "hello";
    /** Value to be present under the key {@code "mock"}. */
    private static final MockObject MOCK_VALUE = new MockObject("mock");

    /**
     * Testing attribute source with {@link #INTEGER_VALUE} and
     * {@link #STRING_VALUE} as request attributes and {@link #MOCK_VALUE} as a
     * context attribute.
     */
    private static final ServletRequest DATA;
    static {
        final ServletContext context = new MockServletContext();
        context.setAttribute("mock", MOCK_VALUE);

        DATA = new MockServletRequest().setServletContext(context);
        DATA.setAttribute("integer", INTEGER_VALUE);
        DATA.setAttribute("string", STRING_VALUE);
    }

    /**
     * Tests {@link ServletSymbol#pull(ServletRequest)}.
     */
    @Test
    public void testPull() {
        final AdaptationProvider<Integer> p = Downcasting.to(Integer.class);
        Assert.assertEquals(new ServletSymbol<>("integer", p).pull(DATA), INTEGER_VALUE);
        Assert.assertEquals(new ServletSymbol<>("string", p).pull(DATA), STRING_VALUE);
        Assert.assertNull(new ServletSymbol<>("missing", p).pull(DATA));

        // @formatter:off
        Assert.assertEquals(new ServletSymbol<>("mock", Downcasting.to(MockObject.class)).pull(DATA), MOCK_VALUE);
        Assert.assertEquals(new ServletSymbol<>("mock", Downcasting.to(MockObject.class)).forContext().pull(DATA.getServletContext()), MOCK_VALUE);
        Assert.assertNull(new ServletSymbol<>("mock", Downcasting.to(MockObject.class)).forRequest().pull(DATA));
        // @formatter:on
    }

    /**
     * Tests {@link ServletSymbol#get(ServletRequest)}.
     */
    @Test
    public void testGet() {
        Assert.assertEquals(new ServletSymbol<>("integer", Downcasting.to(Integer.class)).get(DATA), INTEGER_VALUE);
        Assert.assertEquals(new ServletSymbol<>("mock", Downcasting.to(MockObject.class)).get(DATA), MOCK_VALUE);
        Assert.assertNull(new ServletSymbol<>("string", Downcasting.to(Integer.class)).get(DATA));
        Assert.assertNull(new ServletSymbol<>("missing", Downcasting.to(Object.class)).get(DATA));
    }

    /**
     * Tests {@link ServletSymbol#give(ServletRequest)}.
     */
    @Test
    public void testGive() {
        Assert.assertEquals(new ServletSymbol<>("integer", Downcasting.to(Integer.class)).give(DATA), INTEGER_VALUE);
        Assert.assertEquals(new ServletSymbol<>("mock", Downcasting.to(MockObject.class)).give(DATA), MOCK_VALUE);
        Assert.assertNull(new ServletSymbol<>("string", Downcasting.to(Integer.class)).give(DATA));
        Assert.assertNull(new ServletSymbol<>("missing", Downcasting.to(Object.class)).give(DATA));

        final Integer i = Integer.valueOf(1);
        final AdaptationProvider<Integer> p = Downcasting.withFallbackTo(Integer.class, i);
        Assert.assertEquals(new ServletSymbol<>("integer", p).give(DATA), INTEGER_VALUE);
        Assert.assertEquals(new ServletSymbol<>("string", p).give(DATA), i);
        Assert.assertEquals(new ServletSymbol<>("missing", p).give(DATA), i);
    }

    /**
     * Tests {@link ServletSymbol#find(ServletRequest)}.
     */
    @Test
    public void testFind() {
        final AdaptationProvider<Integer> p = Downcasting.to(Integer.class);
        Assert.assertEquals(new ServletSymbol<>("integer", p).find(DATA).get(), INTEGER_VALUE);
        Assert.assertEquals(new ServletSymbol<>("mock", Downcasting.to(MockObject.class)).find(DATA).get(), MOCK_VALUE);
        Assert.assertFalse(new ServletSymbol<>("string", p).find(DATA).isPresent());
        Assert.assertFalse(new ServletSymbol<>("missing", p).find(DATA).isPresent());
    }

    /**
     * Tests {@link ServletSymbol#yield(ServletRequest)}.
     */
    @Test
    public void testYield() {
        final Integer i = Integer.valueOf(1);
        final AdaptationProvider<Integer> p = Downcasting.withFallbackTo(Integer.class, i);

        final AdaptationResult<Integer> r1 = new ServletSymbol<>("integer", p).yield(DATA);
        Assert.assertEquals(r1.argument(), INTEGER_VALUE);
        Assert.assertEquals(r1.get(), INTEGER_VALUE);
        Assert.assertEquals(r1.fallback().get(), i);

        final AdaptationResult<Integer> r2 = new ServletSymbol<>("string", p).yield(DATA);
        Assert.assertEquals(r2.argument(), STRING_VALUE);
        Assert.assertNull(r2.get());
        Assert.assertEquals(r2.fallback().get(), i);

        final AdaptationResult<Integer> r3 = new ServletSymbol<>("missing", p).yield(DATA);
        Assert.assertEquals(r3.argument(), null);
        Assert.assertNull(r3.get());
        Assert.assertEquals(r3.fallback().get(), i);
    }
}
