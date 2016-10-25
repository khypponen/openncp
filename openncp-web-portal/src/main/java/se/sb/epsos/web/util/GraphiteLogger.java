/***    Copyright 2011-2013 Apotekens Service AB <epsos@apotekensservice.se>
*
*    This file is part of epSOS-WEB.
*
*    epSOS-WEB is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*    epSOS-WEB is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License along with epSOS-WEB. If not, see http://www.gnu.org/licenses/.
**//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.sb.epsos.web.util;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  This is a utillity loger class for logging metrics to a graphite server
 * 
 * @author andreas
 */
public class GraphiteLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphiteLogger.class);
    private String graphiteHost;
    private int graphitePort;
    private boolean enabled;
    private String nodeIdentifier;
    
    public static GraphiteLogger getDefaultLogger() {
        String gHost = MasterConfigManager.get("ApplicationConfigManager.GraphiteLogger.graphiteHost");
        int gPort = MasterConfigManager.getInt("ApplicationConfigManager.GraphiteLogger.graphitePort");
        boolean enabled = FeatureFlagsManager.check(Feature.SEND_METRICS_TO_GRAPHITE);
        return new GraphiteLogger(gHost, gPort, enabled);
    }

    /**
     * 
     * @param graphiteHost  hostname of the graphite server
     * @param graphitePort  any positive integer
     * @param enabled       
     */
    public GraphiteLogger(String graphiteHost, int graphitePort, boolean enabled) {
        this.enabled = enabled;
        this.graphiteHost = graphiteHost;
        this.graphitePort = graphitePort;
        try {
            this.nodeIdentifier = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            LOGGER.warn("Failed to determin host name",ex);
        }
        if (this.graphiteHost==null || this.graphiteHost.length()==0 || 
            this.nodeIdentifier==null || this.nodeIdentifier.length()==0 ||
            this.graphitePort<0 || !logToGraphite("connection.test", 1L)) 
        {
            LOGGER.warn("Faild to create GraphiteLogger, graphiteHost graphitePost or nodeIdentifier could not be defined properly: " + about());
            this.enabled=false;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public final String about() {
        return new StringBuffer().append("{ graphiteHost=").append(this.graphiteHost).append(", graphitePort=").append(this.graphitePort).append(", nodeIdentifier=").append(this.nodeIdentifier).append(" }").toString();
    }
    
    public void logMetric(String key, long value) {
        logToGraphite(key,value);
    }

    public boolean logToGraphite(String key, long value) {
        Map<String, Long> stats = new HashMap<String, Long>();
        stats.put(key, value);
        return logToGraphite(stats);
    }

    public boolean logToGraphite(Map<String, Long> stats) {
        if (stats.isEmpty()) {
            return true;
        }

        try {
            logToGraphite(nodeIdentifier, stats);
        } catch (Throwable t) {
            LOGGER.warn("Can't log to graphite", t);
            return false;
        }
        return true;
    }

    private void logToGraphite(String nodeIdentifier, Map<String, Long> stats) throws Exception {
        Long curTimeInSec = System.currentTimeMillis() / 1000;
        StringBuffer lines = new StringBuffer();
        for (Object entry : stats.entrySet()) {
            Entry<?, ?> stat = (Entry<?, ?>)entry;
            String key = nodeIdentifier + "." + stat.getKey();
            lines.append(key).append(" ").append(stat.getValue()).append(" ").append(curTimeInSec).append("\n"); //even the last line in graphite 
        }
        logToGraphite(lines);
    }

    private void logToGraphite(StringBuffer lines) throws Exception {
        if (this.enabled) {
            String msg = lines.toString();
            LOGGER.debug("Writing [{}] to graphite", msg);
            Socket socket = new Socket(graphiteHost, graphitePort);
            try {
                Writer writer = new OutputStreamWriter(socket.getOutputStream());
                writer.write(msg);
                writer.flush();
                writer.close();
            } finally {
                socket.close();
            }
        }
    }

}
