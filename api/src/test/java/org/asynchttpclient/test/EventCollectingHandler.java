/*
 * Copyright (c) 2014 AsyncHttpClient Project. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.asynchttpclient.test;

import java.net.InetAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.asynchttpclient.AsyncCompletionHandlerBase;
import org.asynchttpclient.HttpResponseHeaders;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.Response;
import org.asynchttpclient.channel.NameResolution;
import org.asynchttpclient.handler.AsyncHandlerExtensions;
import org.testng.Assert;

public class EventCollectingHandler extends AsyncCompletionHandlerBase implements AsyncHandlerExtensions {

    public static final String COMPLETED_EVENT = "Completed";
    public static final String STATUS_RECEIVED_EVENT = "StatusReceived";
    public static final String HEADERS_RECEIVED_EVENT = "HeadersReceived";
    public static final String HEADERS_WRITTEN_EVENT = "HeadersWritten";
    public static final String CONTENT_WRITTEN_EVENT = "ContentWritten";
    public static final String CONNECTION_OPEN_EVENT = "ConnectionOpen";
    public static final String DNS_RESOLVED_EVENT = "DnsResolved";
    public static final String CONNECTION_SUCCESS_EVENT = "ConnectionSuccess";
    public static final String CONNECTION_FAILURE_EVENT = "ConnectionFailure";
    public static final String SSL_HANDSHAKE_COMPLETED_EVENT = "SslHandshakeCompleted";
    public static final String CONNECTION_POOL_EVENT = "ConnectionPool";
    public static final String CONNECTION_POOLED_EVENT = "ConnectionPooled";
    public static final String CONNECTION_OFFER_EVENT = "ConnectionOffer";
    public static final String REQUEST_SEND_EVENT = "RequestSend";
    public static final String RETRY_EVENT = "Retry";

    public Queue<String> firedEvents = new ConcurrentLinkedQueue<>();
    private CountDownLatch completionLatch = new CountDownLatch(1);

    public void waitForCompletion(int timeout, TimeUnit unit) throws InterruptedException {
        if (!completionLatch.await(timeout, unit)) {
            Assert.fail("Timeout out");
        }
    }

    @Override
    public Response onCompleted(Response response) throws Exception {
        firedEvents.add(COMPLETED_EVENT);
        try {
            return super.onCompleted(response);
        } finally {
            completionLatch.countDown();
        }
    }

    @Override
    public State onStatusReceived(HttpResponseStatus status) throws Exception {
        firedEvents.add(STATUS_RECEIVED_EVENT);
        return super.onStatusReceived(status);
    }

    @Override
    public State onHeadersReceived(HttpResponseHeaders headers) throws Exception {
        firedEvents.add(HEADERS_RECEIVED_EVENT);
        return super.onHeadersReceived(headers);
    }

    @Override
    public State onHeadersWritten() {
        firedEvents.add(HEADERS_WRITTEN_EVENT);
        return super.onHeadersWritten();
    }

    @Override
    public State onContentWritten() {
        firedEvents.add(CONTENT_WRITTEN_EVENT);
        return super.onContentWritten();
    }

    @Override
    public void onConnectionOpen() {
        firedEvents.add(CONNECTION_OPEN_EVENT);
    }

    @Override
    public void onDnsResolved(NameResolution[] nameResolutions) {
        firedEvents.add(DNS_RESOLVED_EVENT);
    }

    @Override
    public void onConnectionSuccess(Object connection, InetAddress address) {
        firedEvents.add(CONNECTION_SUCCESS_EVENT);
    }

    @Override
    public void onConnectionFailure(InetAddress address) {
        firedEvents.add(CONNECTION_FAILURE_EVENT);
    }

    @Override
    public void onSslHandshakeCompleted() {
        firedEvents.add(SSL_HANDSHAKE_COMPLETED_EVENT);
    }

    @Override
    public void onConnectionPool() {
        firedEvents.add(CONNECTION_POOL_EVENT);
    }

    @Override
    public void onConnectionPooled(Object connection) {
        firedEvents.add(CONNECTION_POOLED_EVENT);
    }

    @Override
    public void onConnectionOffer(Object connection) {
        firedEvents.add(CONNECTION_OFFER_EVENT);
    }

    @Override
    public void onRequestSend(Object request) {
        firedEvents.add(REQUEST_SEND_EVENT);
    }

    @Override
    public void onRetry() {
        firedEvents.add(RETRY_EVENT);
    }
}
