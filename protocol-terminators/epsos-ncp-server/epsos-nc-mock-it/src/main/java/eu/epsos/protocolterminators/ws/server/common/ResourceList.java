package eu.epsos.protocolterminators.ws.server.common;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * list resources available from the classpath
 * 
 * @author stoughto http://forums.devx.com/showthread.php?153784-how-to-list-resources-in-a-package
 * 
 */
public class ResourceList {

	private static Logger logger = LoggerFactory.getLogger(ResourceList.class);
	/**
	 * for all elements of java.class.path get a Collection of resources Pattern
	 * pattern = Pattern.compile(".*"); gets all resources
	 * 
	 * @param pattern
	 *            the pattern to match
	 * @return the resources in the order they are found
	 */
	public static Collection<String> getResources(Pattern pattern) {

	    ArrayList<String> retval = new ArrayList<>();
		URLClassLoader classLoader = (URLClassLoader)Thread.currentThread().getContextClassLoader();
		URL[] classPathElements = classLoader.getURLs();
		for (URL element : classPathElements) {
			try {
				if(!element.toString().contains("mock")) continue;
				logger.debug("Found URL " + element);
				retval.addAll(getResources(element.toURI(), pattern));
			} catch (URISyntaxException e) {
				logger.debug("Could not convert URL " + element + " into URI");
			}
		}
		return retval;
	}

	private static Collection<String> getResources(URI element,
			Pattern pattern) {

		ArrayList<String> retval = new ArrayList<>();
		File file = new File(element);
		if (file.isDirectory()) {
			retval.addAll(getResourcesFromDirectory(file, pattern));
		} else {
			retval.addAll(getResourcesFromJarFile(file, pattern));
		}
		return retval;
	}

	private static Collection<String> getResourcesFromJarFile(File file,
			Pattern pattern) {

		ArrayList<String> retval = new ArrayList<>();
		ZipFile zf;
		try {
			zf = new ZipFile(file);
		} catch (ZipException e) {
			throw new Error(e);
		} catch (IOException e) {
			throw new Error(e);
		}
		Enumeration e = zf.entries();
		while (e.hasMoreElements()) {
			ZipEntry ze = (ZipEntry) e.nextElement();
			String fileName = ze.getName();
			boolean accept = pattern.matcher(fileName).matches();
			if (accept) {
				retval.add(fileName);
			}
		}
		try {
			zf.close();
		} catch (IOException e1) {
			throw new Error(e1);
		}
		return retval;
	}

	private static Collection<String> getResourcesFromDirectory(File directory,
			Pattern pattern) {

	    ArrayList<String> retval = new ArrayList<>();
		File[] fileList = directory.listFiles();
		for (File file : fileList) {
			if (file.isDirectory()) {
				retval.addAll(getResourcesFromDirectory(file, pattern));
			} else {
				try {
					String fileName = file.getCanonicalPath();
					boolean accept = pattern.matcher(fileName).matches();
					if (accept) {
						retval.add(fileName);
					}
				} catch (IOException e) {
					throw new Error(e);
				}
			}
		}
		return retval;
	}

	/**
	 * list the resources that match args[0]
	 * 
	 * @param args
	 *            args[0] is the pattern to match, or list all resources if
	 *            there are no args
	 */
	public static void main(String[] args) {

	    Pattern pattern;
		if (args.length < 1) {
			pattern = Pattern.compile(".*");
		} else {
			pattern = Pattern.compile(args[0]);
		}
		Collection<String> list = ResourceList.getResources(pattern);
		for (String name : list) {
			System.out.println(name);
		}
	}
}