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

import java.util.Optional;

import javax.servlet.ServletRequest;

import net.yetamine.sova.adaptation.AdaptationResult;
import net.yetamine.sova.adaptation.Mappable;

/**
 * An interface for retrieving data from {@link ServletRequest} attributes which
 * resembles the {@link Mappable} interface, so that it can be seamlessly merged
 * with it.
 *
 * @param <T>
 *            the type of resulting values
 */
public interface ServletRequestValue<T> {

    /**
     * Get the attribute value from the request without any adaptation.
     *
     * @param source
     *            the source to use. It must not be {@code null}.
     *
     * @return the attribute value from the request without any adaptation
     */
    Object fetch(ServletRequest source);

    // Mappable-like methods

    /**
     * Returns an adapted value from the source.
     *
     * @param source
     *            the source of the argument to adapt. It must not be
     *            {@code null}.
     *
     * @return the result of the adaptation, or {@code null} if not possible
     */
    T get(ServletRequest source);

    /**
     * Returns an adapted value from the source, or the default.
     *
     * @param source
     *            the source of the argument to adapt. It must not be
     *            {@code null}.
     *
     * @return the result of the adaptation, or the default
     */
    T use(ServletRequest source);

    /**
     * Returns an adapted value from the source as an {@link Optional}.
     *
     * @param source
     *            the source of the argument to adapt. It must not be
     *            {@code null}.
     *
     * @return an adapted value from the source as an {@link Optional}
     */
    Optional<T> find(ServletRequest source);

    /**
     * Returns a representation of an adapted value from the source.
     *
     * @param source
     *            the source of the argument to adapt. It must not be
     *            {@code null}.
     *
     * @return the result of the adaptation
     */
    AdaptationResult<T> yield(ServletRequest source);
}
