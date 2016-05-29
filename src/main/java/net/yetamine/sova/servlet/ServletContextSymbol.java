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
import java.util.function.Supplier;

import javax.servlet.ServletContext;

import net.yetamine.sova.AdaptationProvider;
import net.yetamine.sova.AdaptationResult;
import net.yetamine.sova.Downcasting;

/**
 * A symbol implementation designed specifically for dealing with instances of
 * {@link ServletContext}.
 *
 * @param <T>
 *            the type of resulting values
 */
public final class ServletContextSymbol<T> extends ServletAttributeSymbol<T> {

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
    public ServletContextSymbol(String attributeName, AdaptationProvider<T> adaptation) {
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
    public ServletContextSymbol(String attributeName, Class<T> type) {
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

        if (obj instanceof ServletContextSymbol<?>) {
            return attribute().equals(((ServletContextSymbol<?>) obj).attribute());
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

    // Mappable-like methods

    /**
     * Get the attribute value from the request without any adaptation.
     *
     * @param source
     *            the source to use. It must not be {@code null}.
     *
     * @return the attribute value from the request without any adaptation
     */
    public Object pull(ServletContext source) {
        return source.getAttribute(attribute());
    }

    /**
     * Transfers the given value to the given consumer.
     *
     * @param consumer
     *            the consumer to accept the attribute. It must not be
     *            {@code null}.
     * @param value
     *            the value to transfer
     */
    public void push(ServletContext consumer, T value) {
        consumer.setAttribute(attribute(), value);
    }

    /**
     * Returns an adapted value from the source.
     *
     * @param source
     *            the source of the argument to adapt. It must not be
     *            {@code null}.
     *
     * @return the result of the adaptation, or {@code null} if not possible
     */
    public T get(ServletContext source) {
        return nullable(pull(source));
    }

    /**
     * Returns an adapted value from the source, or the default.
     *
     * @param source
     *            the source of the argument to adapt. It must not be
     *            {@code null}.
     *
     * @return the result of the adaptation, or the default
     */
    public T give(ServletContext source) {
        return surrogate(pull(source));
    }

    /**
     * Returns an adapted value from the source as an {@link Optional}.
     *
     * @param source
     *            the source of the argument to adapt. It must not be
     *            {@code null}.
     *
     * @return an adapted value from the source as an {@link Optional}
     */
    public Optional<T> find(ServletContext source) {
        return optional(pull(source));
    }

    /**
     * Returns a representation of an adapted value from the source.
     *
     * @param source
     *            the source of the argument to adapt. It must not be
     *            {@code null}.
     *
     * @return the result of the adaptation
     */
    public AdaptationResult<T> yield(ServletContext source) {
        return adapt(pull(source));
    }

    /**
     * Puts the adapted value to the given map.
     *
     * @param consumer
     *            the map to accept the {@link #remap()} result and the adapted
     *            value. It must not be {@code null}.
     * @param value
     *            the value to adapt and transfer
     */
    public void put(ServletContext consumer, Object value) {
        push(consumer, nullable(value));
    }

    /**
     * Puts the specified value to the given map, or removes the existing value
     * from the map if the specified value could not be adapted to a valid
     * object.
     *
     * @param consumer
     *            the map to accept the {@link #remap()} result and the adapted
     *            value. It must not be {@code null}.
     * @param value
     *            the value to adapt and transfer
     */
    public void let(ServletContext consumer, Object value) {
        final T result = nullable(value);

        if (result == null) { // Null or non-adaptable
            consumer.removeAttribute(attribute());
            return;
        }

        push(consumer, result);
    }

    /**
     * Transfers the adapted value to the given consumer if the value could be
     * adapted to a valid object.
     *
     * @param consumer
     *            the consumer to accept the {@link #remap()} result and the
     *            adapted value. It must not be {@code null}.
     * @param value
     *            the value to adapt and transfer
     *
     * @return the result of the adaptation
     */
    public Optional<T> have(ServletContext consumer, Object value) {
        final Optional<T> result = optional(value);
        result.ifPresent(v -> push(consumer, v));
        return result;
    }

    /**
     * Returns a value from the source if the source can supply a valid result,
     * otherwise fixes the source with a surrogate value and returns it instead.
     * The returned value should be then present in the source in either case.
     *
     * @param source
     *            the source to provide or accept the value. It must not be
     *            {@code null}.
     * @param surrogate
     *            the surrogate supplier. It must not be {@code null}.
     *
     * @return the original or surrogate value, which the source contains now;
     *         {@code null} may be returned if the surrogate does not pass the
     *         adaptation
     */
    public T supply(ServletContext source, Supplier<? extends T> surrogate) {
        final T current = get(source);
        if (current != null) {
            return current;
        }

        final T result = nullable(surrogate.get());
        if (result == null) { // Null or non-adaptable
            source.removeAttribute(attribute());
            return null;
        }

        push(source, result);
        return result;
    }

    /**
     * Puts the default to the source if the attribute does not provide an
     * adaptable value and returns the adaptation of the value then.
     *
     * @param source
     *            the source of the argument to adapt and to store the result.
     *            It must not be {@code null}.
     *
     * @return the result of adaptation, or the default; {@code null} may be
     *         returned if the fallback does not return anything better
     */
    public T supply(ServletContext source) {
        return supply(source, fallback());
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
    public Optional<T> have(ServletContext source) {
        final Object current = pull(source);

        if (current != null) { // If present, try to use it
            return optional(current);
        }

        final Optional<T> result = Optional.ofNullable(fallback().get());
        result.ifPresent(value -> push(source, value));
        return result;
    }

    /**
     * @see net.yetamine.sova.symbols.AbstractSymbol#introspect(java.util.Map)
     */
    @Override
    protected void introspect(Map<Object, Object> result) {
        super.introspect(result);
        result.put(toString("attribute@context"), attribute());
    }
}
