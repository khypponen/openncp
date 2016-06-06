package eu.ehealth.ccd.smp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Some general purpose utilities
 * 
 * @author joao.cunha
 *
 */
public final class GenericUtils {
	private GenericUtils() {}

	/* Returns a file's creation time. Used for the search mask */
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
    
    public static String printExceptionStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder(e.toString());
        for (StackTraceElement ste : e.getStackTrace()) {
            sb.append("\n\tat ");
            sb.append(ste);
        }
        String trace = sb.toString();
        return trace;
    }
    
    public static String convertFileToString(File file) throws IOException {
		Scanner s = new Scanner(file);
		String contents = s.useDelimiter("\\Z").next();
		s.close();
		return contents;
    }
}
