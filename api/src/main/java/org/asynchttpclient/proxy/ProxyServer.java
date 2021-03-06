/*
 * Copyright 2010 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */
package org.asynchttpclient.proxy;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.asynchttpclient.Realm;
import org.asynchttpclient.Realm.AuthScheme;

/**
 * Represents a proxy server.
 */
public class ProxyServer {

    public enum Protocol {
        HTTP("HTTP"), NTLM("NTLM"), KERBEROS("KERBEROS"), SPNEGO("SPNEGO");

        private final String protocol;

        Protocol(final String protocol) {
            this.protocol = protocol;
        }

        public String getProtocol() {
            return protocol;
        }

        @Override
        public String toString() {
            return getProtocol();
        }
    }

    private final List<String> nonProxyHosts = new ArrayList<>();
    private final Protocol protocol;
    private final String host;
    private final String principal;
    private final String password;
    private final int port;
    private final int securedPort;
    private Charset charset = UTF_8;
    private String ntlmDomain = System.getProperty("http.auth.ntlm.domain");
    private String ntlmHost;
    private AuthScheme scheme;
    private boolean forceHttp10 = false;

    public ProxyServer(Protocol protocol, String host, int port, int securedPort, String principal, String password) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.securedPort = securedPort;
        this.principal = principal;
        this.password = password;
        this.scheme = principal == null ? AuthScheme.NONE : AuthScheme.BASIC;
    }
    
    public ProxyServer(Protocol protocol, String host, int port, String principal, String password) {
        this(protocol, host, port, port, principal, password);
    }

    public ProxyServer(String host, int port, String principal, String password) {
        this(Protocol.HTTP, host, port, principal, password);
    }

    public ProxyServer(final Protocol protocol, final String host, final int port) {
        this(protocol, host, port, null, null);
    }

    public ProxyServer(final String host, final int port) {
        this(Protocol.HTTP, host, port, null, null);
    }

    public Realm.RealmBuilder realmBuilder() {
        return new Realm.RealmBuilder()//
        .setNtlmDomain(ntlmDomain)
        .setNtlmHost(ntlmHost)
        .setPrincipal(principal)
        .setPassword(password)
        .setScheme(scheme);
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getSecuredPort() {
        return securedPort;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getPassword() {
        return password;
    }

    public ProxyServer setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    public ProxyServer addNonProxyHost(String uri) {
        nonProxyHosts.add(uri);
        return this;
    }

    public ProxyServer removeNonProxyHost(String uri) {
        nonProxyHosts.remove(uri);
        return this;
    }

    public List<String> getNonProxyHosts() {
        return Collections.unmodifiableList(nonProxyHosts);
    }

    public ProxyServer setNtlmDomain(String ntlmDomain) {
        this.ntlmDomain = ntlmDomain;
        return this;
    }

    public AuthScheme getScheme() {
        return scheme;
    }

    public void setScheme(AuthScheme scheme) {
        if (principal == null)
            throw new NullPointerException("principal");
        if (password == null)
            throw new NullPointerException("password");
        this.scheme = scheme;
    }

    public void setNtlmHost(String ntlmHost) {
        this.ntlmHost = ntlmHost;
    }
    
    public boolean isForceHttp10() {
        return forceHttp10;
    }

    public void setForceHttp10(boolean forceHttp10) {
        this.forceHttp10 = forceHttp10;
    }
}
