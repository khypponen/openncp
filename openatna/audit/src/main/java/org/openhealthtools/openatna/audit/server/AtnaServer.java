/**
 *  Copyright (c) 2009-2011 University of Cardiff and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    University of Cardiff - initial API and implementation
 *    -
 */

package org.openhealthtools.openatna.audit.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openatna.audit.server.nio.TcpNioServer;
import org.openhealthtools.openatna.audit.server.nio.UdpNioServer;
import org.openhealthtools.openatna.net.IConnectionDescription;
import org.openhealthtools.openatna.syslog.SyslogException;
import org.openhealthtools.openatna.syslog.SyslogMessage;
import org.openhealthtools.openatna.syslog.transport.SyslogListener;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Andrew Harrison
 * @date $Date:$ modified by $Author:$
 */

public class AtnaServer {

    private static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.server.AtnaServer");

    private IConnectionDescription tlsConnection;
    private IConnectionDescription udpConnection;
    private Server tcpServer = null;
    private Server udpServer = null;
    private boolean nio = false;
    private MessageQueue queue = null;

    private ExecutorService exec;

    public AtnaServer(IConnectionDescription tlsConnection, IConnectionDescription udpConnection, int threads, boolean nio) {
        this.tlsConnection = tlsConnection;
        this.udpConnection = udpConnection;
        this.nio = nio;
        exec = Executors.newFixedThreadPool(threads);
    }

    public AtnaServer(IConnectionDescription tlsConnection, IConnectionDescription udpConnection) {
        this(tlsConnection, udpConnection, 5, false);
    }

    public void start(SyslogListener listener) throws IOException {
        queue = new MessageQueue(listener);
        queue.start();
        if (tlsConnection != null) {
            if (nio) {
                tcpServer = new TcpNioServer(this, tlsConnection);
            } else {
                tcpServer = new TcpServer(this, tlsConnection);
            }
            tcpServer.start();
        }
        if (udpConnection != null) {
            if (nio) {
                udpServer = new UdpNioServer(this, udpConnection);
            } else {
                udpServer = new UdpServer(this, udpConnection);
            }
            udpServer.start();
        }
    }

    public void stop() throws IOException {
        log.info("AtnaServer shutting down...");
    	if (tcpServer != null) {
            tcpServer.stop();
        }
        if (udpServer != null) {
            udpServer.stop();
        }
        if (queue != null) {
            queue.stop();
        }
        
        exec.shutdown();
    }

    public void execute(Runnable r) {
        exec.execute(r);
    }

    public IConnectionDescription getUdpConnection() {
        return this.udpConnection;
    }

    public IConnectionDescription getTlsConnection() {
        return this.tlsConnection;
    }

    public void notifyListeners(final SyslogMessage msg) {
        if (queue != null) {
            queue.put(msg);
        }
    }

    public void notifyException(final SyslogException ex) {
        if (queue != null) {
            queue.put(ex);
        }
    }
}
