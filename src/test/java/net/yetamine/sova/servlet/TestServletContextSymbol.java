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

import java.util.function.Supplier;

import javax.servlet.ServletContext;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.yetamine.sova.AdaptationProvider;
import net.yetamine.sova.AdaptationResult;
import net.yetamine.sova.Downcasting;

/**
 * Tests {@link ServletContextSymbol} (actually, just the new methods, not the
 * inherited ones).
 */
public final class TestServletContextSymbol {

    /** Value to be present under the key {@code "integer"}. */
    private static final Integer INTEGER_VALUE = Integer.valueOf(1024);
    /** Value to be present under the key {@code "string"}. */
    private static final String STRING_VALUE = "hello";

    /**
     * Testing attribute source with both {@link #INTEGER_VALUE} and
     * {@link #STRING_VALUE}.
     */
    private static final ServletContext DATA;
    static {
        DATA = new MockServletContext();
        DATA.setAttribute("integer", INTEGER_VALUE);
        DATA.setAttribute("string", STRING_VALUE);
    }

    /**
     * Tests {@link ServletContextSymbol#pull(ServletContext)}.
     */
    @Test
    public void testPull() {
        final AdaptationProvider<Integer> p = Downcasting.to(Integer.class);
        Assert.assertEquals(new ServletContextSymbol<>("integer", p).pull(DATA), INTEGER_VALUE);
        Assert.assertEquals(new ServletContextSymbol<>("string", p).pull(DATA), STRING_VALUE);
        Assert.assertNull(new ServletContextSymbol<>("missing", p).pull(DATA));
    }

    /**
     * Tests {@link ServletContextSymbol#push(ServletContext, Object)}.
     */
    @Test
    public void testPush() {
        final MockObject i = new MockObject(INTEGER_VALUE);
        final AdaptationProvider<MockObject> p = Downcasting.to(MockObject.class);
        final ServletContextSymbol<MockObject> symbol = new ServletContextSymbol<>("test", p);

        final ServletContext data = new MockServletContext();
        symbol.push(data, i);
        Assert.assertSame(symbol.pull(data), i);
    }

    /**
     * Tests {@link ServletContextSymbol#get(ServletContext)}.
     */
    @Test
    public void testGet() {
        // @formatter:off
        Assert.assertEquals(new ServletContextSymbol<>("integer", Downcasting.to(Integer.class)).get(DATA), INTEGER_VALUE);
        Assert.assertNull(new ServletContextSymbol<>("string", Downcasting.to(Integer.class)).get(DATA));
        Assert.assertNull(new ServletContextSymbol<>("missing", Downcasting.to(Object.class)).get(DATA));
        // @formatter:on
    }

    /**
     * Tests {@link ServletContextSymbol#give(ServletContext)}.
     */
    @Test
    public void testGive() {
        // @formatter:off
        Assert.assertEquals(new ServletContextSymbol<>("integer", Downcasting.to(Integer.class)).give(DATA), INTEGER_VALUE);
        Assert.assertNull(new ServletContextSymbol<>("string", Downcasting.to(Integer.class)).give(DATA));
        Assert.assertNull(new ServletContextSymbol<>("missing", Downcasting.to(Object.class)).give(DATA));

        final Integer i = Integer.valueOf(1);
        final AdaptationProvider<Integer> adaptation = Downcasting.withFallbackTo(Integer.class, i);
        Assert.assertEquals(new ServletContextSymbol<>("integer", adaptation).give(DATA), INTEGER_VALUE);
        Assert.assertEquals(new ServletContextSymbol<>("string", adaptation).give(DATA), i);
        Assert.assertEquals(new ServletContextSymbol<>("missing", adaptation).give(DATA), i);
        // @formatter:on
    }

    /**
     * Tests {@link ServletContextSymbol#find(ServletContext)}.
     */
    @Test
    public void testFind() {
        // @formatter:off
        Assert.assertEquals(new ServletContextSymbol<>("integer", Downcasting.to(Integer.class)).find(DATA).get(), INTEGER_VALUE);
        Assert.assertFalse(new ServletContextSymbol<>("string", Downcasting.to(Integer.class)).find(DATA).isPresent());
        Assert.assertFalse(new ServletContextSymbol<>("missing", Downcasting.to(Object.class)).find(DATA).isPresent());
        // @formatter:on
    }

    /**
     * Tests {@link ServletContextSymbol#yield(ServletContext)}.
     */
    @Test
    public void testYield() {
        final Integer i = Integer.valueOf(1);
        final AdaptationProvider<Integer> adaptation = Downcasting.withFallbackTo(Integer.class, i);

        final AdaptationResult<Integer> r1 = new ServletContextSymbol<>("integer", adaptation).yield(DATA);
        Assert.assertEquals(r1.argument(), INTEGER_VALUE);
        Assert.assertEquals(r1.get(), INTEGER_VALUE);
        Assert.assertEquals(r1.fallback().get(), i);

        final AdaptationResult<Integer> r2 = new ServletContextSymbol<>("string", adaptation).yield(DATA);
        Assert.assertEquals(r2.argument(), STRING_VALUE);
        Assert.assertNull(r2.get());
        Assert.assertEquals(r2.fallback().get(), i);

        final AdaptationResult<Integer> r3 = new ServletContextSymbol<>("missing", adaptation).yield(DATA);
        Assert.assertEquals(r3.argument(), null);
        Assert.assertNull(r3.get());
        Assert.assertEquals(r3.fallback().get(), i);
    }

    /**
     * Tests {@link ServletContextSymbol#put(ServletContext, Object)}.
     */
    @Test
    public void testPut() {
        final AdaptationProvider<Integer> adaptation = Downcasting.to(Integer.class);
        final ServletContext m = new MockServletContext(DATA);
        final Integer i = Integer.valueOf(1);

        new ServletContextSymbol<>("integer", adaptation).put(m, i);
        Assert.assertEquals(m.getAttribute("integer"), i);

        new ServletContextSymbol<>("string", adaptation).put(m, i);
        Assert.assertEquals(m.getAttribute("string"), i);

        new ServletContextSymbol<>("missing", adaptation).put(m, i);
        Assert.assertEquals(m.getAttribute("missing"), i);
    }

    /**
     * Tests {@link ServletContextSymbol#have(ServletContext, Object)} and
     * {@link ServletContextSymbol#have(ServletContext)}.
     */
    @Test
    public void testHave() {
        final AdaptationProvider<Integer> adaptation = Downcasting.to(Integer.class);
        final ServletContext m = new MockServletContext(DATA);
        final Integer i = Integer.valueOf(1);

        Assert.assertEquals(new ServletContextSymbol<>("integer", adaptation).have(m, i).get(), i);
        Assert.assertEquals(m.getAttribute("integer"), i);

        Assert.assertEquals(new ServletContextSymbol<>("string", adaptation).have(m, i).get(), i);
        Assert.assertEquals(m.getAttribute("string"), i);

        Assert.assertEquals(new ServletContextSymbol<>("missing", adaptation).have(m, i).get(), i);
        Assert.assertEquals(m.getAttribute("missing"), i);

        Assert.assertFalse(new ServletContextSymbol<>("integer", adaptation).have(m, "hello").isPresent());
        Assert.assertEquals(m.getAttribute("integer"), i);

        Assert.assertFalse(new ServletContextSymbol<>("none", adaptation).have(m, "hello").isPresent());
        Assert.assertNull(m.getAttribute("none"));

        // Additional 'have' overload
        final Integer j = Integer.valueOf(2);
        final AdaptationProvider<Integer> having = Downcasting.withFallbackTo(Integer.class, j);
        Assert.assertEquals(new ServletContextSymbol<>("none", having).have(m).get(), j);

    }

    /**
     * Tests {@link ServletContextSymbol#let(ServletContext, Object)}.
     */
    @Test
    public void testLet() {
        final AdaptationProvider<Integer> adaptation = Downcasting.to(Integer.class);
        final ServletContext m = new MockServletContext(DATA);
        final Integer i = Integer.valueOf(1);

        new ServletContextSymbol<>("integer", adaptation).let(m, i);
        Assert.assertEquals(m.getAttribute("integer"), i);

        new ServletContextSymbol<>("string", adaptation).let(m, i);
        Assert.assertEquals(m.getAttribute("string"), i);

        new ServletContextSymbol<>("missing", adaptation).let(m, i);
        Assert.assertEquals(m.getAttribute("missing"), i);

        new ServletContextSymbol<>("integer", adaptation).let(m, null);
        Assert.assertNull(m.getAttribute("integer"));

        new ServletContextSymbol<>("string", adaptation).let(m, "hello");
        Assert.assertNull(m.getAttribute("string"));
    }

    /**
     * Tests {@link ServletContextSymbol#supply(ServletContext, Supplier)}.
     */
    @Test
    public void testSupply() {
        final AdaptationProvider<Integer> adaptation = Downcasting.to(Integer.class);
        final ServletContext m = new MockServletContext(DATA);
        final Integer i = Integer.valueOf(1);

        Assert.assertEquals(new ServletContextSymbol<>("integer", adaptation).supply(m, () -> i), INTEGER_VALUE);
        Assert.assertEquals(m.getAttribute("integer"), INTEGER_VALUE);

        Assert.assertEquals(new ServletContextSymbol<>("string", adaptation).supply(m, () -> i), i);
        Assert.assertEquals(m.getAttribute("string"), i);

        Assert.assertEquals(new ServletContextSymbol<>("missing", adaptation).supply(m, () -> i), i);
        Assert.assertEquals(m.getAttribute("missing"), i);
    }
}
