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
import java.util.Optional;

import javax.servlet.ServletRequest;

import net.yetamine.sova.AdaptationProvider;
import net.yetamine.sova.AdaptationResult;
import net.yetamine.sova.Downcasting;

/**
 * A symbol implementation designed specifically for dealing with instances of
 * {@link ServletRequest}.
 *
 * @param <T>
 *            the type of resulting values
 */
public final class ServletRequestSymbol<T> extends ServletAttributeSymbol<T> implements ServletRequestValue<T> {

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
    public ServletRequestSymbol(String attributeName, AdaptationProvider<T> adaptation) {
        super(attributeName, adaptation);
    }

    /**
     * Creates a new instance using {@link Downcasting#to(Class)}.
     *
     * @param attributeName
     *            the name of the attribute which this instance is bound to. It
     *            must not be {@code null}.
     * @param type
     *            the desired type of resulting values. It must not be
     *            {@code null}.
     */
    public ServletRequestSymbol(String attributeName, Class<T> type) {
        this(attributeName, Downcasting.to(type));
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ServletRequestSymbol<?>) {
            return attribute().equals(((ServletRequestSymbol<?>) obj).attribute());
        }

        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return attribute().hashCode();
    }

    /**
     * @see net.yetamine.sova.servlet.ServletRequestValue#fetch(javax.servlet.ServletRequest)
     */
    public Object fetch(ServletRequest source) {
        return source.getAttribute(attribute());
    }

    // Mappable-like methods

    /**
     * @see net.yetamine.sova.servlet.ServletRequestValue#get(javax.servlet.ServletRequest)
     */
    public T get(ServletRequest source) {
        return derive(fetch(source));
    }

    /**
     * @see net.yetamine.sova.servlet.ServletRequestValue#use(javax.servlet.ServletRequest)
     */
    public T use(ServletRequest source) {
        return recover(fetch(source));
    }

    /**
     * @see net.yetamine.sova.servlet.ServletRequestValue#find(javax.servlet.ServletRequest)
     */
    public Optional<T> find(ServletRequest source) {
        return resolve(fetch(source));
    }

    /**
     * Puts the default to the source if the attribute does not provide an
     * adaptable value and returns the adaptation of the value then.
     *
     * @param source
     *            the source of the argument to adapt and to store the result.
     *            It must not be {@code null}.
     *
     * @return the result of adaptation, or {@code null} if there is no default
     */
    public T let(ServletRequest source) {
        final T current = get(source);
        if (current != null) {
            return current;
        }

        final T result = fallback().get();
        put(source, result);
        return result;
    }

    /**
     * Puts the default to the source if the attribute is absent, otherwise
     * tries to use the present attribute to get the result.
     *
     * @param source
     *            the source of the result, which might be possibly updated. It
     *            must not be {@code null}.
     *
     * @return the adaptation of the resulting value; an empty container is
     *         returned if the value could not adapted, or no default is
     *         provided
     */
    public Optional<T> have(ServletRequest source) {
        final Object current = fetch(source);

        if (current != null) { // If present, try to use it
            return Optional.ofNullable(derive(current));
        }

        final Optional<T> result = Optional.ofNullable(fallback().get());
        result.ifPresent(value -> put(source, value));
        return result;
    }

    /**
     * @see net.yetamine.sova.servlet.ServletRequestValue#yield(javax.servlet.ServletRequest)
     */
    public AdaptationResult<T> yield(ServletRequest source) {
        return adapt(fetch(source));
    }

    /**
     * Transfers the given value to the given consumer.
     *
     * @param consumer
     *            the consumer to accept the {@link #remap()} result and the
     *            given value. It must not be {@code null}.
     * @param value
     *            the value to transfer
     */
    public void put(ServletRequest consumer, T value) {
        consumer.setAttribute(attribute(), value);
    }

    /**
     * Transfers the adapted value to the given consumer.
     *
     * @param consumer
     *            the consumer to accept the {@link #remap()} result and the
     *            adapted value. It must not be {@code null}.
     * @param value
     *            the value to adapt and transfer
     */
    public void set(ServletRequest consumer, Object value) {
        put(consumer, derive(value));
    }

    /**
     * @see net.yetamine.sova.symbols.AbstractSymbol#introspect(java.util.Map)
     */
    @Override
    protected void introspect(Map<Object, Object> result) {
        super.introspect(result);
        result.put(toString("attribute@request"), attribute());
    }
}
