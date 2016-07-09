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

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import net.yetamine.sova.AdaptationProvider;
import net.yetamine.sova.AdaptationResult;
import net.yetamine.sova.AdaptationStrategy;
import net.yetamine.sova.symbols.DelegatingSymbol;

/**
 * A symbol allowing to access attributes from both {@link ServletRequest} and
 * {@link ServletContext} instances.
 *
 * <p>
 * When this class is used for reading an attribute from a request, it tries to
 * get it from the request at first and if no values found, it tries to get the
 * attribute from the request's context. This fallback search is useful when an
 * attribute may be specified in a request or in its context, having the global
 * default values in the context, while request-specific overriding values in
 * the request.
 *
 * @param <T>
 *            the type of resulting values
 */
public final class ServletSymbol<T> extends DelegatingSymbol<T> implements ServletRequestValue<T> {

    /** Symbol for processing requests. */
    private final ServletRequestSymbol<T> requestSymbol;
    /** Symbol for processing contexts. */
    private final ServletContextSymbol<T> contextSymbol;

    /**
     * Creates a new instance.
     *
     * @param attributeName
     *            the name of the attribute which this instance is bound to. It
     *            must not be {@code null}.
     * @param adaptation
     *            the adaptation of the attribute value. It must not be
     *            {@code null}.
     */
    public ServletSymbol(String attributeName, AdaptationProvider<T> adaptation) {
        super(adaptation);
        requestSymbol = new ServletRequestSymbol<>(attributeName, adaptation);
        contextSymbol = new ServletContextSymbol<>(attributeName, adaptation);
    }

    /**
     * Creates a new instance for an attribute that has different names for the
     * request and context home.
     *
     * @param requestAttribute
     *            the attribute for the request processing. It must not be
     *            {@code null}.
     * @param contextAttribute
     *            the attribute for the context processing. It must not be
     *            {@code null}.
     * @param adaptation
     *            the adaptation of the attribute value. It must not be
     *            {@code null}.
     */
    public ServletSymbol(String requestAttribute, String contextAttribute, AdaptationStrategy<T> adaptation) {
        super(adaptation);
        requestSymbol = new ServletRequestSymbol<>(requestAttribute, adaptation);
        contextSymbol = new ServletContextSymbol<>(contextAttribute, adaptation);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ServletSymbol<?>) {
            final ServletSymbol<?> o = (ServletSymbol<?>) obj;
            return requestSymbol.equals(o.requestSymbol) && contextSymbol.equals(o.contextSymbol);
        }

        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(requestSymbol, contextSymbol);
    }

    /**
     * Returns the symbol for context processing.
     *
     * @return the symbol for context processing
     */
    public ServletContextSymbol<T> forContext() {
        return contextSymbol;
    }

    /**
     * Returns the symbol for request processing.
     *
     * @return the symbol for request processing
     */
    public ServletRequestSymbol<T> forRequest() {
        return requestSymbol;
    }

    /* Implementation note:
     *
     * Following methods can't use the usual pattern of just invoking a mappable
     * method like 'return nullable(fetch(source))' for get, because the request
     * might return a non-null value that could not be adapted, resulting in a
     * premature fallback.
     */

    /**
     * @see net.yetamine.sova.servlet.ServletRequestValue#pull(javax.servlet.ServletRequest)
     */
    public Object pull(ServletRequest source) {
        final Object result = requestSymbol.pull(source);
        return (result != null) ? result : contextSymbol.pull(source.getServletContext());
    }

    /**
     * @see net.yetamine.sova.servlet.ServletRequestValue#get(javax.servlet.ServletRequest)
     */
    public T get(ServletRequest source) {
        final T result = requestSymbol.get(source);
        return (result != null) ? result : contextSymbol.get(source.getServletContext());
    }

    /**
     * @see net.yetamine.sova.servlet.ServletRequestValue#give(javax.servlet.ServletRequest)
     */
    public T give(ServletRequest source) {
        final T result = requestSymbol.get(source);
        return (result != null) ? result : contextSymbol.give(source.getServletContext());
    }

    /**
     * @see net.yetamine.sova.servlet.ServletRequestValue#find(javax.servlet.ServletRequest)
     */
    public Optional<T> find(ServletRequest source) {
        final T result = requestSymbol.get(source);
        return (result != null) ? Optional.of(result) : contextSymbol.find(source.getServletContext());
    }

    /**
     * @see net.yetamine.sova.servlet.ServletRequestValue#yield(javax.servlet.ServletRequest)
     */
    public AdaptationResult<T> yield(ServletRequest source) {
        final AdaptationResult<T> result1 = requestSymbol.yield(source);
        if (result1.isPresent()) {
            return result1;
        }

        final AdaptationResult<T> result2 = contextSymbol.yield(source.getServletContext());
        if (result2.isPresent()) {
            return result2;
        }

        return (result1.argument() != null) ? result1 : result2;
    }

    /**
     * @see net.yetamine.sova.symbols.ExpansiveSymbol#introspect(java.util.Map)
     */
    @Override
    protected void introspect(Map<Object, Object> result) {
        super.introspect(result);
        result.put("attribute@request", requestSymbol.attribute());
        result.put("attribute@context", contextSymbol.attribute());
    }
}
