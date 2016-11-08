package eu.ehealth.ccd.smp;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;

/**
 * Some general purpose utilities
 * 
 * @author joao.cunha
 *
 */
public final class GenericUtils {
    private GenericUtils() {}

    /**
     * Returns a file's creation time. Used for the search mask
     * @param filePath Path to the file to be processed
     * @return File creation time
     * @throws IOException 
     */
    public static String getCreationTime(String filePath) throws IOException {
        Path p = Paths.get(filePath);
        BasicFileAttributes view
           = Files.getFileAttributeView(p, BasicFileAttributeView.class)
                  .readAttributes();
//      String creationTime = view.creationTime().toString();
//      System.out.println(creationTime +" is the same as "+view.lastModifiedTime());
        
        // confirm with tests if the date is correctly generated
        long millis = view.creationTime().toMillis();
        Date creationDate = new Date(millis);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String dateString = df.format(creationDate);
//      System.out.println(dateString);
        return dateString;
    }
    
    /**
     * Pretty-prints an Exception into its stack trace
     * @param e The Exception instance that we want to print the stack trace
     * @return The string representation of the Exception's stack trace
     */
    public static String printExceptionStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder(e.toString());
        for (StackTraceElement ste : e.getStackTrace()) {
            sb.append("\n\tat ");
            sb.append(ste);
        }
        String trace = sb.toString();
        return trace;
    }
    
    /**
     * Writes the content of a file into a String
     * @param file The file to be read and printed
     * @return The String representation of the content of the file
     * @throws IOException 
     */
    public static String convertFileToString(File file) throws IOException {
        Scanner s = new Scanner(file);
        String contents = s.useDelimiter("\\Z").next();
        s.close();
        return contents;
    }
    
    /**
     * Converts a w3c Element into a pretty-printed String
     * @param elem The Element to be converted into a pretty-printed String
     * @return The indented XML String representation of the element
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     */
    public static String convertElementToString(Element elem) throws TransformerConfigurationException, TransformerException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        StringWriter buffer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(elem),
              new StreamResult(buffer));
        String str = buffer.toString();
        return str;
    }
}
