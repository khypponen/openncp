package eu.epsos.protocolterminators.ws.server.common;

import eu.epsos.protocolterminators.ws.server.xca.impl.DocumentSearchMockImpl;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

/**
 * Helper class for loading file resources from the classpath.
 *
 * @author gareth
 */
public class ResourceLoader {
    public static Logger logger = Logger.getLogger(ResourceLoader.class);

    /**
     * Locates and loads file for the specified filename. Returns contents as a
     * String.
     *
     * @param resourceName Filename (without path)
     * @return
     */
    public String getResource(String resourceName) {
        String resourceStr = "";

        ClassLoader cl = getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(resourceName);

        if (is != null) {
            resourceStr = streamToString(is);
            //logger.debug(resourceStr);
        }

        if (resourceStr.isEmpty()) {
            logger.debug("ERROR - Failed to load resource with name " + resourceName);
        }

        return resourceStr;
    }

    private String streamToString(InputStream in) {
        StringBuilder out = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                out.append(line);
            }
            br.close();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toString();
    }
    
    public byte[] getResourceAsByteArray(String resourceName) {
        ClassLoader cl = getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream(resourceName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[100000];
        int i = 0;
        try {
			while ((i = is.read(buff, 0, buff.length)) > 0) {
			    baos.write(buff, 0, i);
			}
	        is.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
        return baos.toByteArray();
    }
}
