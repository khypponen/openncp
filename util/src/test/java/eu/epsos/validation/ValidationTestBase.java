package eu.epsos.validation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ValidationTestBase {
    protected String getResource(String filename) {
	ClassLoader loader = getClass().getClassLoader();
	InputStream inputStream = loader.getResourceAsStream(filename);
	StringBuilder builder = new StringBuilder();
	try {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	    for (String line = reader.readLine(); line != null; line = reader.readLine()) {
		builder.append(line);
	    }
	    reader.close();
	    inputStream.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return builder.toString();
    }
}
