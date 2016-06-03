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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServletRequest;

/**
 * A mock for {@link HttpServletRequest} useful just for attributes holding.
 */
final class MockServletContext implements ServletContext {

    /** Attribute holder. */
    private final Map<String, Object> attributes = new HashMap<>();

    /**
     * Creates a new instance.
     */
    public MockServletContext() {
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
     * @see javax.servlet.ServletContext#getAttributeNames()
     */
    public Enumeration<String> getAttributeNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getContextPath()
     */
    public String getContextPath() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getContext(java.lang.String)
     */
    public ServletContext getContext(String uripath) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getMajorVersion()
     */
    public int getMajorVersion() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getMinorVersion()
     */
    public int getMinorVersion() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getEffectiveMajorVersion()
     */
    public int getEffectiveMajorVersion() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getEffectiveMinorVersion()
     */
    public int getEffectiveMinorVersion() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
     */
    public String getMimeType(String file) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    public Set<String> getResourcePaths(String path) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getResource(java.lang.String)
     */
    public URL getResource(String path) throws MalformedURLException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
     */
    public RequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getServlet(java.lang.String)
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getServlets()
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public Enumeration<Servlet> getServlets() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getServletNames()
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public Enumeration<String> getServletNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#log(java.lang.String)
     */
    public void log(String msg) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#log(java.lang.Exception,
     *      java.lang.String)
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public void log(Exception exception, String msg) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#log(java.lang.String,
     *      java.lang.Throwable)
     */
    public void log(String message, Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     */
    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getServerInfo()
     */
    public String getServerInfo() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
     */
    public String getInitParameter(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getInitParameterNames()
     */
    public Enumeration<String> getInitParameterNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#setInitParameter(java.lang.String,
     *      java.lang.String)
     */
    public boolean setInitParameter(String name, String value) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    public String getServletContextName() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#addServlet(java.lang.String,
     *      java.lang.String)
     */
    public Dynamic addServlet(String servletName, String className) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#addServlet(java.lang.String,
     *      javax.servlet.Servlet)
     */
    public Dynamic addServlet(String servletName, Servlet servlet) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#addServlet(java.lang.String,
     *      java.lang.Class)
     */
    public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#createServlet(java.lang.Class)
     */
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getServletRegistration(java.lang.String)
     */
    public ServletRegistration getServletRegistration(String servletName) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getServletRegistrations()
     */
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#addFilter(java.lang.String,
     *      java.lang.String)
     */
    public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, String className) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#addFilter(java.lang.String,
     *      javax.servlet.Filter)
     */
    public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#addFilter(java.lang.String,
     *      java.lang.Class)
     */
    public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#createFilter(java.lang.Class)
     */
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getFilterRegistration(java.lang.String)
     */
    public FilterRegistration getFilterRegistration(String filterName) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getFilterRegistrations()
     */
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getSessionCookieConfig()
     */
    public SessionCookieConfig getSessionCookieConfig() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#setSessionTrackingModes(java.util.Set)
     */
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getDefaultSessionTrackingModes()
     */
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getEffectiveSessionTrackingModes()
     */
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#addListener(java.lang.String)
     */
    public void addListener(String className) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#addListener(java.util.EventListener)
     */
    public <T extends EventListener> void addListener(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#addListener(java.lang.Class)
     */
    public void addListener(Class<? extends EventListener> listenerClass) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#createListener(java.lang.Class)
     */
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getJspConfigDescriptor()
     */
    public JspConfigDescriptor getJspConfigDescriptor() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getClassLoader()
     */
    public ClassLoader getClassLoader() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#declareRoles(java.lang.String[])
     */
    public void declareRoles(String... roleNames) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.servlet.ServletContext#getVirtualServerName()
     */
    public String getVirtualServerName() {
        throw new UnsupportedOperationException();
    }
}
