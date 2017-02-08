/**
 * Copyright (C) 2011, 2012 SRDC Yazilim Arastirma ve Gelistirme ve Danismanlik Tic. Ltd. Sti. <epsos@srdc.com.tr>
 * <p>
 * This file is part of SRDC epSOS NCP.
 * <p>
 * SRDC epSOS NCP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * SRDC epSOS NCP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with SRDC epSOS NCP. If not, see <http://www.gnu.org/licenses/>.
 */
package tr.com.srdc.epsos.util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.srdc.epsos.util.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class IPUtil {

    public static Logger logger = LoggerFactory.getLogger(IPUtil.class);

    /**
     * This static method depends on the automation page of whatismyip.com
     * @return
     */
    public static String getMyPublicIP() {

        String ip_address = "127.0.0.1";

        try {
            URL autoIP = new URL("http://automation.whatismyip.com/n09230945.asp");
            BufferedReader in = new BufferedReader(new InputStreamReader(autoIP.openStream()));
            ip_address = (in.readLine()).trim();

        } catch (Exception e) {
            logger.error("whatismyip.com was not accessible for learning our IP", e);
        }

        return ip_address;
    }

    /**
     * This returns the SERVER_IP value from the epsos-srdc.properties config file.
     * @return
     */
    public static String getServerIPStatic() {
        return Constants.SERVER_IP;
    }

}
