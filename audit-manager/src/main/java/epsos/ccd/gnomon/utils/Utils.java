package epsos.ccd.gnomon.utils;

import epsos.ccd.gnomon.configmanager.ConfigurationManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.net.URL;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);
    private static final String EMPTY_VALUE = "";

    private Utils() {
    }

    public static String getProperty(String key) {
        return getProperty(key, EMPTY_VALUE);
    }

    public static String getProperty(String key, String defaultValue) {
        return getProperty(key, defaultValue, false);
    }

    public synchronized static String getProperty(String key, String defaultValue, boolean persistIfNotFound) {
        try {
            String value = ConfigurationManagerService.getInstance().getProperty(key);
            if (isEmpty(value)) {
                value = defaultValue;
                if (persistIfNotFound) {
                    ConfigurationManagerService.getInstance().updateProperty(key, value);
                }
            }

            return value;
        } catch (Exception e) {
            log.error("Error occurred while fetching property: " + e.getMessage(), e);
            return defaultValue;
        }
    }

    public synchronized static void writeXMLToFile(String am, String path) {
        BufferedWriter out = null;
        FileWriter fstream = null;
        try {
            fstream = new FileWriter(path);
            out = new BufferedWriter(fstream);
            out.write(am);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            Utils.close(out);
            Utils.close(fstream);
        }
    }

    /**
     * Given a string containing an xml structure, it parses it and returns the
     * dom object
     *
     * @param inputFile the xml string
     * @return org.w3c.dom.Document
     */
    public static Document createDomFromString(String inputFile) {
        Document doc = null;
        // Instantiate the document to be signed
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            doc = dbFactory.newDocumentBuilder().parse(Utils.StringToStream(inputFile));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return doc;

    }

    /**
     * Validates an xml file against XMLSchema
     *
     * @param XmlDocumentUrl is the path of the tsl file
     * @param url            the url of the schema file
     * @return true/false if the source xml is valid against the rfc3881 xsd
     */
    public static boolean validateSchema(String xmlDocumentUrl, URL url) throws SAXException, IOException {

        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = factory.newSchema(url);
        javax.xml.validation.Validator validator = schema.newValidator();
        Source source = new StreamSource(StringToStream(xmlDocumentUrl));

        try {
            validator.validate(source);
            log.info("document is valid");
            return true;
        } catch (SAXException e) {
            log.error("document is not valid because ");
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                close(is);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return sb.toString();
    }

    /**
     * Convert String to InputString using ByteArrayInputStream class. This
     * class constructor takes the string byte array which can be done by
     * calling the getBytes() method.
     *
     * @param text
     * @return the input stream
     */
    public static InputStream StringToStream(String text) {
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(text.getBytes());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return is;
    }

    /**
     * <p>
     * Creates an InputStream from a file, and fills it with the complete file.
     * Thus, available() on the returned InputStream will return the full number
     * of bytes the file contains
     * </p>
     *
     * @param fname The filename
     * @return The filled InputStream
     * @throws IOException , if the Streams couldn't be created.
     **/
    public static InputStream fullStream(String fname) throws IOException {
        DataInputStream dis = null;
        FileInputStream fis = null;
        ByteArrayInputStream bais = null;

        try {
            fis = new FileInputStream(fname);
            dis = new DataInputStream(fis);
            byte[] bytes = new byte[dis.available()];
            dis.readFully(bytes);
            bais = new ByteArrayInputStream(bytes);
            return bais;
        } finally {
            close(bais);
            close(dis);
            close(fis);
        }
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {
            log.warn("Unable to close.", e);
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.warn("Sleep interrupted.");
        }
    }
}
