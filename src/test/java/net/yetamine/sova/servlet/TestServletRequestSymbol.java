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

import javax.servlet.ServletRequest;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.yetamine.sova.AdaptationProvider;
import net.yetamine.sova.AdaptationResult;
import net.yetamine.sova.Downcasting;

/**
 * Tests {@link ServletRequestSymbol} (actually, just the new methods, not the
 * inherited ones).
 */
public final class TestServletRequestSymbol {

    // Testing values
    private static final Integer INTEGER_VALUE = Integer.valueOf(1024);
    private static final String STRING_VALUE = "hello";

    private static final ServletRequest DATA;
    static {
        DATA = new MockServletRequest();
        DATA.setAttribute("integer", INTEGER_VALUE);
        DATA.setAttribute("string", STRING_VALUE);
    }

    /**
     * Tests {@link ServletRequestSymbol#pull(ServletRequest)}.
     */
    @Test
    public void testPull() {
        final AdaptationProvider<Integer> p = Downcasting.to(Integer.class);
        Assert.assertEquals(new ServletRequestSymbol<>("integer", p).pull(DATA), INTEGER_VALUE);
        Assert.assertEquals(new ServletRequestSymbol<>("string", p).pull(DATA), STRING_VALUE);
        Assert.assertNull(new ServletRequestSymbol<>("missing", p).pull(DATA));
    }

    /**
     * Tests {@link ServletRequestSymbol#push(ServletRequest, Object)}.
     */
    @Test
    public void testPush() {
        final MockObject i = new MockObject(INTEGER_VALUE);
        final AdaptationProvider<MockObject> p = Downcasting.to(MockObject.class);
        final ServletRequestSymbol<MockObject> symbol = new ServletRequestSymbol<>("test", p);

        final ServletRequest data = new MockServletRequest();
        symbol.push(data, i);
        Assert.assertSame(symbol.pull(data), i);
    }

    /**
     * Tests {@link ServletRequestSymbol#get(ServletRequest)}.
     */
    @Test
    public void testGet() {
        // @formatter:off
        Assert.assertEquals(new ServletRequestSymbol<>("integer", Downcasting.to(Integer.class)).get(DATA), INTEGER_VALUE);
        Assert.assertNull(new ServletRequestSymbol<>("string", Downcasting.to(Integer.class)).get(DATA));
        Assert.assertNull(new ServletRequestSymbol<>("missing", Downcasting.to(Object.class)).get(DATA));
        // @formatter:on
    }

    /**
     * Tests {@link ServletRequestSymbol#give(ServletRequest)}.
     */
    @Test
    public void testGive() {
        // @formatter:off
        Assert.assertEquals(new ServletRequestSymbol<>("integer", Downcasting.to(Integer.class)).give(DATA), INTEGER_VALUE);
        Assert.assertNull(new ServletRequestSymbol<>("string", Downcasting.to(Integer.class)).give(DATA));
        Assert.assertNull(new ServletRequestSymbol<>("missing", Downcasting.to(Object.class)).give(DATA));

        final Integer i = Integer.valueOf(1);
        final AdaptationProvider<Integer> adaptation = Downcasting.withFallbackTo(Integer.class, i);
        Assert.assertEquals(new ServletRequestSymbol<>("integer", adaptation).give(DATA), INTEGER_VALUE);
        Assert.assertEquals(new ServletRequestSymbol<>("string", adaptation).give(DATA), i);
        Assert.assertEquals(new ServletRequestSymbol<>("missing", adaptation).give(DATA), i);
        // @formatter:on
    }

    /**
     * Tests {@link ServletRequestSymbol#find(ServletRequest)}.
     */
    @Test
    public void testFind() {
        // @formatter:off
        Assert.assertEquals(new ServletRequestSymbol<>("integer", Downcasting.to(Integer.class)).find(DATA).get(), INTEGER_VALUE);
        Assert.assertFalse(new ServletRequestSymbol<>("string", Downcasting.to(Integer.class)).find(DATA).isPresent());
        Assert.assertFalse(new ServletRequestSymbol<>("missing", Downcasting.to(Object.class)).find(DATA).isPresent());
        // @formatter:on
    }

    /**
     * Tests {@link ServletRequestSymbol#yield(ServletRequest)}.
     */
    @Test
    public void testYield() {
        final Integer i = Integer.valueOf(1);
        final AdaptationProvider<Integer> adaptation = Downcasting.withFallbackTo(Integer.class, i);

        final AdaptationResult<Integer> r1 = new ServletRequestSymbol<>("integer", adaptation).yield(DATA);
        Assert.assertEquals(r1.argument(), INTEGER_VALUE);
        Assert.assertEquals(r1.get(), INTEGER_VALUE);
        Assert.assertEquals(r1.fallback().get(), i);

        final AdaptationResult<Integer> r2 = new ServletRequestSymbol<>("string", adaptation).yield(DATA);
        Assert.assertEquals(r2.argument(), STRING_VALUE);
        Assert.assertNull(r2.get());
        Assert.assertEquals(r2.fallback().get(), i);

        final AdaptationResult<Integer> r3 = new ServletRequestSymbol<>("missing", adaptation).yield(DATA);
        Assert.assertEquals(r3.argument(), null);
        Assert.assertNull(r3.get());
        Assert.assertEquals(r3.fallback().get(), i);
    }

    /**
     * Tests {@link ServletRequestSymbol#put(ServletRequest, Object)}.
     */
    @Test
    public void testPut() {
        final AdaptationProvider<Integer> adaptation = Downcasting.to(Integer.class);
        final ServletRequest m = new MockServletRequest(DATA);
        final Integer i = Integer.valueOf(1);

        new ServletRequestSymbol<>("integer", adaptation).put(m, i);
        Assert.assertEquals(m.getAttribute("integer"), i);

        new ServletRequestSymbol<>("string", adaptation).put(m, i);
        Assert.assertEquals(m.getAttribute("string"), i);

        new ServletRequestSymbol<>("missing", adaptation).put(m, i);
        Assert.assertEquals(m.getAttribute("missing"), i);
    }

    /**
     * Tests {@link ServletRequestSymbol#have(ServletRequest, Object)} and
     * {@link ServletRequestSymbol#have(ServletRequest)}.
     */
    @Test
    public void testHave() {
        final AdaptationProvider<Integer> adaptation = Downcasting.to(Integer.class);
        final ServletRequest m = new MockServletRequest(DATA);
        final Integer i = Integer.valueOf(1);

        Assert.assertEquals(new ServletRequestSymbol<>("integer", adaptation).have(m, i).get(), i);
        Assert.assertEquals(m.getAttribute("integer"), i);

        Assert.assertEquals(new ServletRequestSymbol<>("string", adaptation).have(m, i).get(), i);
        Assert.assertEquals(m.getAttribute("string"), i);

        Assert.assertEquals(new ServletRequestSymbol<>("missing", adaptation).have(m, i).get(), i);
        Assert.assertEquals(m.getAttribute("missing"), i);

        Assert.assertFalse(new ServletRequestSymbol<>("integer", adaptation).have(m, "hello").isPresent());
        Assert.assertEquals(m.getAttribute("integer"), i);

        Assert.assertFalse(new ServletRequestSymbol<>("none", adaptation).have(m, "hello").isPresent());
        Assert.assertNull(m.getAttribute("none"));

        // Additional 'have' overload
        final Integer j = Integer.valueOf(2);
        final AdaptationProvider<Integer> having = Downcasting.withFallbackTo(Integer.class, j);
        Assert.assertEquals(new ServletRequestSymbol<>("none", having).have(m).get(), j);

    }

    /**
     * Tests {@link ServletRequestSymbol#let(ServletRequest, Object)}.
     */
    @Test
    public void testLet() {
        final AdaptationProvider<Integer> adaptation = Downcasting.to(Integer.class);
        final ServletRequest m = new MockServletRequest(DATA);
        final Integer i = Integer.valueOf(1);

        new ServletRequestSymbol<>("integer", adaptation).let(m, i);
        Assert.assertEquals(m.getAttribute("integer"), i);

        new ServletRequestSymbol<>("string", adaptation).let(m, i);
        Assert.assertEquals(m.getAttribute("string"), i);

        new ServletRequestSymbol<>("missing", adaptation).let(m, i);
        Assert.assertEquals(m.getAttribute("missing"), i);

        new ServletRequestSymbol<>("integer", adaptation).let(m, null);
        Assert.assertNull(m.getAttribute("integer"));

        new ServletRequestSymbol<>("string", adaptation).let(m, "hello");
        Assert.assertNull(m.getAttribute("string"));
    }

    /**
     * Tests {@link ServletRequestSymbol#supply(ServletRequest, Supplier)}.
     */
    @Test
    public void testSupply() {
        final AdaptationProvider<Integer> adaptation = Downcasting.to(Integer.class);
        final ServletRequest m = new MockServletRequest(DATA);
        final Integer i = Integer.valueOf(1);

        Assert.assertEquals(new ServletRequestSymbol<>("integer", adaptation).supply(m, () -> i), INTEGER_VALUE);
        Assert.assertEquals(m.getAttribute("integer"), INTEGER_VALUE);

        Assert.assertEquals(new ServletRequestSymbol<>("string", adaptation).supply(m, () -> i), i);
        Assert.assertEquals(m.getAttribute("string"), i);

        Assert.assertEquals(new ServletRequestSymbol<>("missing", adaptation).supply(m, () -> i), i);
        Assert.assertEquals(m.getAttribute("missing"), i);
    }
}
