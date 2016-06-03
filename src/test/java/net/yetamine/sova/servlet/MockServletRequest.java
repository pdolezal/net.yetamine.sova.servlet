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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * A mock for {@link ServletRequest} useful just for attributes holding.
 */
final class MockServletRequest implements ServletRequest {

    /** Attribute holder. */
    private final Map<String, Object> attributes = new HashMap<>();

    /**
     * Creates a new instance.
     */
    public MockServletRequest() {
        // Default constructor
    }

    // Methods needed by the implementation

    /**
     * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * @see javax.servlet.ServletRequest#setAttribute(java.lang.String,
     *      java.lang.Object)
     */
    public void setAttribute(String name, Object o) {
        attributes.put(name, o);
    }

    /**
     * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    // Methods not important for the tests

    /**
     * @see javax.servlet.ServletRequest#getAttributeNames()
     */
    public Enumeration<String> getAttributeNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
     */
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    public int getContentLength() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getContentLengthLong()
     */
    public long getContentLengthLong() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getContentType()
     */
    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    public ServletInputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
     */
    public String getParameter(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getParameterNames()
     */
    public Enumeration<String> getParameterNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    public String[] getParameterValues(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getParameterMap()
     */
    public Map<String, String[]> getParameterMap() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getProtocol()
     */
    public String getProtocol() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getScheme()
     */
    public String getScheme() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getServerName()
     */
    public String getServerName() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getServerPort()
     */
    public int getServerPort() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getReader()
     */
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getRemoteAddr()
     */
    public String getRemoteAddr() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getRemoteHost()
     */
    public String getRemoteHost() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getLocale()
     */
    public Locale getLocale() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getLocales()
     */
    public Enumeration<Locale> getLocales() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#isSecure()
     */
    public boolean isSecure() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getRemotePort()
     */
    public int getRemotePort() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalName()
     */
    public String getLocalName() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalAddr()
     */
    public String getLocalAddr() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getLocalPort()
     */
    public int getLocalPort() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getServletContext()
     */
    public ServletContext getServletContext() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#startAsync()
     */
    public AsyncContext startAsync() throws IllegalStateException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#startAsync(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse)
     */
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#isAsyncStarted()
     */
    public boolean isAsyncStarted() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#isAsyncSupported()
     */
    public boolean isAsyncSupported() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getAsyncContext()
     */
    public AsyncContext getAsyncContext() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletRequest#getDispatcherType()
     */
    public DispatcherType getDispatcherType() {
        throw new UnsupportedOperationException();
    }
}
