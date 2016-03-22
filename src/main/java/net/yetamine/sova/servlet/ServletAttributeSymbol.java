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

import java.util.Objects;

import net.yetamine.sova.AdaptationProvider;
import net.yetamine.sova.Mappable;
import net.yetamine.sova.Substitutable;
import net.yetamine.sova.symbols.DelegatingSymbol;

/**
 * A symbol base for implementing symbols for various servlet-related attributes
 * that have unique string names.
 *
 * @param <T>
 *            the type of resulting values
 */
public abstract class ServletAttributeSymbol<T> extends DelegatingSymbol<T> implements Substitutable<Mappable<String, T>> {

    /** Name of the attribute. */
    private final String attribute;
    /** Cached {@link #substitute()}. */
    private Mappable<String, T> substitute;

    /**
     * Prepares a new instance.
     *
     * @param attributeName
     *            the name of the attribute which this instance is bound to. It
     *            must not be {@code null}.
     * @param adaptation
     *            the adaptation of the attribute value. It must not be
     *            {@code null}.
     */
    protected ServletAttributeSymbol(String attributeName, AdaptationProvider<T> adaptation) {
        super(adaptation);
        attribute = Objects.requireNonNull(attributeName);
    }

    /**
     * Makes a qualified name.
     *
     * @param qualifier
     *            the qualifying space for the local part of the name. It must
     *            not be {@code null}.
     * @param identifier
     *            the local part of the name. It must not be {@code null}.
     *
     * @return a qualified name consisting of both parts
     */
    public static String name(String qualifier, String identifier) {
        final StringBuilder result = new StringBuilder(qualifier.length() + identifier.length() + 1);
        result.append(qualifier).append(':').append(identifier);
        return result.toString();
    }

    /**
     * Makes a qualified name.
     *
     * @param qualifier
     *            the qualifying space for the local part of the name. It must
     *            not be {@code null}.
     * @param identifier
     *            the local part of the name. It must not be {@code null}.
     *
     * @return a qualified name consisting of both parts
     */
    public static String name(Class<?> qualifier, String identifier) {
        return name(qualifier.getTypeName(), identifier);
    }

    /**
     * @return the identifier of this instance
     */
    public final String attribute() {
        return attribute;
    }

    /**
     * @see net.yetamine.sova.symbols.PublicSymbol#substitute()
     */
    public final Mappable<String, T> substitute() {
        // Using caching technique that uses out-of-thin air thread safety;
        // this technique is alright here, because the instances are always
        // behaving in the same way, therefore they are interchangeable
        Mappable<String, T> result = substitute;
        if (result != null) {
            return result;
        }

        result = Mappable.of(attribute, this);
        substitute = result;
        return result;
    }
}
