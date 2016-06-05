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

/**
 * A test object for adapting and yet useful for equality and identity tests.
 */
final class MockObject {

    /** Value of the object. */
    private final Object value;

    /**
     * Creates a new instance.
     *
     * @param o
     *            the object to wrap
     */
    public MockObject(Object o) {
        value = o;
    }

    /**
     * Creates a new instance with {@code null} value.
     */
    public MockObject() {
        this(null);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MockObject) && Objects.equals(((MockObject) obj).value, value);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return Objects.toString(value);
    }

    /**
     * Returns the value.
     *
     * @return the value
     */
    public Object value() {
        return value;
    }
}
