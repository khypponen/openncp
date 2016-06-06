package com.gnomon.epsos.model.cda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.exception.ExceptionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Utils {

    private static Logger _log = Logger.getLogger("UTILS");
    private static final String HmacMD5 = "HmacMD5";

    public static String checkString(Object s) {
        String refStr = " ";
        try {
            refStr = s.toString();
        } catch (Exception e) {
            _log.error(ExceptionUtils.getStackTrace(e));
        }
        return refStr;
    }

    public static final String escapeHTML(String s) {
        StringBuilder sb = new StringBuilder();
        try {

            int n = s.length();
            for (int i = 0; i < n; i++) {
                char c = s.charAt(i);
                switch (c) {
                    case '<':
                        sb.append("&lt;");
                        break;
                    case '>':
                        sb.append("&gt;");
                        break;
                    case '&':
                        sb.append("&amp;");
                        break;
                    case '"':
                        sb.append("&quot;");
                        break;
                    case 'à':
                        sb.append("&agrave;");
                        break;
                    case 'À':
                        sb.append("&Agrave;");
                        break;
                    case 'â':
                        sb.append("&acirc;");
                        break;
                    case 'Â':
                        sb.append("&Acirc;");
                        break;
                    case 'ä':
                        sb.append("&auml;");
                        break;
                    case 'Ä':
                        sb.append("&Auml;");
                        break;
                    case 'å':
                        sb.append("&aring;");
                        break;
                    case 'Å':
                        sb.append("&Aring;");
                        break;
                    case 'æ':
                        sb.append("&aelig;");
                        break;
                    case 'Æ':
                        sb.append("&AElig;");
                        break;
                    case 'ç':
                        sb.append("&ccedil;");
                        break;
                    case 'Ç':
                        sb.append("&Ccedil;");
                        break;
                    case 'é':
                        sb.append("&eacute;");
                        break;
                    case 'É':
                        sb.append("&Eacute;");
                        break;
                    case 'è':
                        sb.append("&egrave;");
                        break;
                    case 'È':
                        sb.append("&Egrave;");
                        break;
                    case 'ê':
                        sb.append("&ecirc;");
                        break;
                    case 'Ê':
                        sb.append("&Ecirc;");
                        break;
                    case 'ë':
                        sb.append("&euml;");
                        break;
                    case 'Ë':
                        sb.append("&Euml;");
                        break;
                    case 'ï':
                        sb.append("&iuml;");
                        break;
                    case 'Ï':
                        sb.append("&Iuml;");
                        break;
                    case 'ô':
                        sb.append("&ocirc;");
                        break;
                    case 'Ô':
                        sb.append("&Ocirc;");
                        break;
                    case 'ö':
                        sb.append("&ouml;");
                        break;
                    case 'Ö':
                        sb.append("&Ouml;");
                        break;
                    case 'ø':
                        sb.append("&oslash;");
                        break;
                    case 'Ø':
                        sb.append("&Oslash;");
                        break;
                    case 'ß':
                        sb.append("&szlig;");
                        break;
                    case 'ù':
                        sb.append("&ugrave;");
                        break;
                    case 'Ù':
                        sb.append("&Ugrave;");
                        break;
                    case 'û':
                        sb.append("&ucirc;");
                        break;
                    case 'Û':
                        sb.append("&Ucirc;");
                        break;
                    case 'ü':
                        sb.append("&uuml;");
                        break;
                    case 'Ü':
                        sb.append("&Uuml;");
                        break;
                    case '®':
                        sb.append("&reg;");
                        break;
                    case '©':
                        sb.append("&copy;");
                        break;
                    case '€':
                        sb.append("&euro;");
                        break;
                    // be carefull with this one (non-breaking whitee space)
                    //case ' ': sb.append("&nbsp;");break;

                    default:
                        sb.append(c);
                        break;
                }

            }
        } catch (Exception e) {
            _log.error(ExceptionUtils.getStackTrace(e));
        }
        return sb.toString();
    }

    public static String getDocumentAsXml(org.w3c.dom.Document doc, boolean header) {
        String resp = "";
        try {
            DOMSource domSource = new DOMSource(doc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            String omit = "yes";
            if (header) {
                omit = "no";
            } else {
                omit = "yes";
            }
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omit);
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            //transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
            // we want to pretty format the XML output
            // note : this is broken in jdk1.5 beta!
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //
            java.io.StringWriter sw = new java.io.StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(domSource, sr);
            resp = sw.toString();
        } catch (Exception e) {
            _log.error("Problem getting xml as dom");
            _log.error(ExceptionUtils.getStackTrace(e));
        }
        return resp;
    }

    public static String getDocumentAsXml(org.w3c.dom.Document doc) {
        String resp = "";
        if (doc != null) {
            resp = getDocumentAsXml(doc, true);
        }
        return resp;

    }

    public static String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            _log.debug("nodeToString Transformer Exception");
        }
        return sw.toString();
    }

    public static org.w3c.dom.Document ResultsetToXML(java.sql.ResultSet rs) {
        org.w3c.dom.Document doc = null;
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            org.w3c.dom.Element results = doc.createElement("NewDataSet");
            doc.appendChild(results);

            int rowCount = 0;
            int j = 1;
            while (rs.next()) {
                org.w3c.dom.Element row = doc.createElement("Table");
                rowCount++;
                //row.setAttribute("id", j+"");
                results.appendChild(row);
                for (int i = 1; i <= colCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    Object value = rs.getObject(i);
                    org.w3c.dom.Element node = doc.createElement(columnName);
                    String valueStr = "";
                    try {
                        valueStr = escapeHTML(value.toString());
                    } catch (Exception e) {
                        _log.error("Error getting content for column : " + columnName + ". Error dsrc: " + e.getMessage());
                    }
                    node.appendChild(doc.createTextNode(valueStr));
                    row.appendChild(node);
                }
                j++;
            }
            // records
//            org.w3c.dom.Element records = doc.createElement("records");
//            records.appendChild(doc.createTextNode(j+""));
//            results.appendChild(records);
            if (rowCount < 1) {
                return null;
            }
        } catch (Exception e) {
            _log.error("Problem converting sql to xml " + e.getMessage());
            _log.error(ExceptionUtils.getStackTrace(e));
        }
        return doc;
    }

    public static InputStream StringToStream(String text) {
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(text.getBytes());
        } catch (Exception e) {

        }
        return is;
    }

    public static Document createDomFromString(String inputFile) {
        Document doc = null;
        // Instantiate the document to be signed
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            doc = dbFactory
                    .newDocumentBuilder()
                    .parse(StringToStream(inputFile));
        } catch (Exception e) {

        }
        return doc;

    }

    /*
     *
     * <CodeSystem oid="2" displayName="EOF">
     <ValueSet oid="2" displayName="EOF">
     <Entry code="125890301" displayName="LEXOTANIL"/>
     <Entry code="196340101" displayName="ZIRTEK"/>
     <Entry code="076130401" displayName="FLAGYL"/>
     </ValueSet>
     </CodeSystem>
     *
     */
    public static org.w3c.dom.Document ResultsetToXMLWithAttr(Document doc, Element elem, java.sql.ResultSet rs, String oid, String displayName)
            throws SQLException, ParserConfigurationException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
//          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//          DocumentBuilder builder = factory.newDocumentBuilder();
//          org.w3c.dom.Document doc = builder.newDocument();
//          org.w3c.dom.Element codes = doc.createElement("Codes");
//          doc.appendChild(codes);
        org.w3c.dom.Element results = doc.createElement("CodeSystem");
        results.setAttribute("oid", oid);
        results.setAttribute("displayName", displayName);
        elem.appendChild(results);
        int j = 1;
        org.w3c.dom.Element row = doc.createElement("ValueSet");
        row.setAttribute("oid", oid);
        row.setAttribute("displayName", displayName);

        while (rs.next()) {
            //row.setAttribute("id", j+"");
            results.appendChild(row);
            org.w3c.dom.Element node = null;
            node = doc.createElement("Entry");
            for (int i = 1; i <= colCount; i++) {
                String columnName = rsmd.getColumnName(i);
                Object value = rs.getObject(i);
                String valueStr = "";
                try {
                    valueStr = escapeHTML(value.toString());
                } catch (Exception e) {
                }
                node.setAttribute(columnName, valueStr);
                //node.appendChild(doc.createTextNode(valueStr));
            }
            row.appendChild(node);
            j++;
        }
        return doc;
    }

    public static void WriteXMLToFile(String xml, String filename) {
        try {
            // Create file
            File dir1 = new File(".");
            //FileWriter fstream = new FileWriter(dir1.getCanonicalFile() + "/logs/files/" + filename);
            FileWriter fstream = new FileWriter(filename);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(xml);
            //Close the output stream
            out.close();
            fstream.close();
        } catch (Exception e) {//Catch exception if any
            _log.error("Error: " + e.getMessage());
        }
    }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        /*
         * To convert the InputStream to String we use the
         * Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to
         * read. We use the StringWriter class to produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public static String getCDA(String base64String) throws IOException {
        String cdaxml = "";
        ByteArrayOutputStream bazip = new ByteArrayOutputStream();
        int BUFFER_SIZE = 4096;
        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream input = new Base64InputStream(new ByteArrayInputStream(base64String.getBytes()));
        //ByteArrayOutputStream output = new ByteArrayOutputStream();
        int n = input.read(buffer, 0, BUFFER_SIZE);
        while (n >= 0) {
            bazip.write(buffer, 0, n);
            n = input.read(buffer, 0, BUFFER_SIZE);
        }
        input.close();
        // read zip file
        ZipEntry entry;
        ByteArrayOutputStream contents = new ByteArrayOutputStream();
        try {
            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bazip.toByteArray()));
            while ((entry = zis.getNextEntry()) != null) {
                int size;

                byte[] buf = new byte[4096];
                int len, totalLen = 0;

                while ((len = zis.read(buf)) > 0) {
                    contents.write(buf, 0, len);
                    totalLen += len;
                }

            }//while

            zis.close();

        } catch (IOException e) {
            _log.error(ExceptionUtils.getStackTrace(e));
        }

        return contents.toString();

    }

    public static String getZipPostData(ServletRequest req) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = req.getInputStream();
        } catch (IOException e11) {
            _log.error(ExceptionUtils.getStackTrace(e11));
        }
        OutputStream out = new FileOutputStream(new File("/home/karkaletsis/newfile.zip"));
        IOUtils.copy(inputStream, out);
        StringBuilder sb = new StringBuilder();
        ZipEntry entry;
        String data;
        ByteArrayOutputStream contents = new ByteArrayOutputStream();
        byte buf1[] = new byte[1024];
        int letti;

        while ((letti = inputStream.read(buf1)) > 0) {
            contents.write(buf1, 0, letti);
        }

//		data = new String(contents.toByteArray());
//
//		_log.debug("Data : " + data);
        try {
            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(contents.toByteArray()));
            while ((entry = zis.getNextEntry()) != null) {
                int size;

                byte[] buf = new byte[4096];
                int len, totalLen = 0;

                while ((len = zis.read(buf)) > 0) {
                    contents.write(buf, 0, len);
                    totalLen += len;
                }

            }//while

            zis.close();

        } catch (IOException e) {
            _log.error(ExceptionUtils.getStackTrace(e));
        }
        return contents.toString();

//	    String workString="";
//	    try {
//	    	BufferedReader b = new BufferedReader(req.getReader());
//	    	StringBuffer workBuffer = new StringBuffer();
//	        while((workString = b.readLine()) != null) {
//	                workBuffer.append(workString);
//	         }
//	        workString = workBuffer.toString();
//	         //_log.debug("TransportHTTPServlet - Got XML: " + workString);
//		} catch (IOException e1) {
//			log.error(ExceptionUtils.getStackTrace(e1));
//		}
//	   return workString;
    }

    public static String getPostData(ServletRequest req) {
        String workString = "";
        try {
            BufferedReader b = new BufferedReader(req.getReader());
            StringBuilder workBuffer = new StringBuilder();
            while ((workString = b.readLine()) != null) {
                workBuffer.append(workString);
            }
            //   b.reset();
            workString = workBuffer.toString();
            //_log.debug("TransportHTTPServlet - Got XML: " + workString);
        } catch (IOException e1) {
            _log.error(ExceptionUtils.getStackTrace(e1));
        }
        return workString;
    }

    public static InetAddress remoteIp(final HttpServletRequest request)
            throws UnknownHostException {
        if (request.getHeader("x-forwarded-for") != null) {
            return InetAddress.getByName(request.getHeader("x-forwarded-for"));
        }

        return InetAddress.getByName(request.getRemoteAddr());
    }

//
//	public static String getProperty(Configuration cfg, String key)
//	{
//		String value = "";
//		try
//		{
//		// Configuration config = new PropertiesConfiguration("api.properties");
//		//Configuration config = (Configuration) servletRequest.getAttribute("apiproperties");
//		value= cfg.getString(key);
//		_log.debug("VALUE OF " + key + " = " + value);
//		}
//		catch (Exception e)
//		{
//		_log.error(e.getMessage());
//		}
//		return value;
//	}
    public static String clearURL(String url) {
        url = url.replace("/", "");
        url = url.replace(":", "");
        return url;
    }

    public static String encodePDF(byte[] file) {
        String encodedBytes = "";
        try {
            encodedBytes = Base64.encodeBase64String(file);
            _log.warn("encodedBytes " + encodedBytes);
        } catch (Exception e) {
            _log.error(ExceptionUtils.getStackTrace(e));
        }
        return encodedBytes;
    }

    public static void decodePDF(String file) {
        byte[] decodedBytes;
        FileOutputStream fos = null;
        try {
            decodedBytes = Base64.decodeBase64(file);
            fos = new FileOutputStream("/home/karkaletsis/Documents/test.pdf");
            fos.write(decodedBytes);
        } catch (Exception e) {
            _log.error(ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                _log.error(ExceptionUtils.getStackTrace(e));
            }
        }
    }

    public static String formatDateHL7(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String s = formatter.format(date);
        return s;
    }

    public static String convertDateWithPattern(String dateStr, String format, String pattern) {
        //String pattern = "yyyy-MM-dd HH:mm:ss";
        String newstring = "";
        Date date;
        try {
            date = new SimpleDateFormat(pattern).parse(dateStr);
            newstring = new SimpleDateFormat(format).format(date);
        } catch (ParseException e) {
            _log.error("Error convertng date " + dateStr + " with format " + format + " and pattern " + pattern);
        }

        return newstring;

    }

    public static String convertDate(String dateStr, String format) {
        String retDate = "19700101";
        String pattern = "yyyyMMdd";
        try {
            retDate = convertDateWithPattern(dateStr, format, pattern);
        } catch (Exception e) {
            _log.error("Error converting date " + dateStr);
        }
        return retDate;
    }

    public static byte[] readFully(InputStream stream) throws IOException {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }

    public static byte[] loadFile(String sourcePath) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourcePath);
            return readFully(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

}
