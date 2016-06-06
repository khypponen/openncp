package epsos.ccd.carecom.tsam.synchronizer.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import epsos.ccd.carecom.tsam.synchronizer.MissingSystemPropertyException;

/**
 * This class implements the access to the settings using a singleton design patterns. 
 * It uses the <b>double-checked locking</b> * to reduce the use of synchronization in the getInstance() 
 * method. The method is thread-safe.
 */
public class Settings {
	
	/**
	 * Holds a thread safe reference to the unique setting object.
	 */
	private volatile static Settings uniqueInstance;
	
	private static final String VALUE_TRUNCATE = "TRUNCATE";
	private static final String VALUE_PERSIST = "PERSIST";
	
	/**
	 * Holds the default file name of the file containing the properties that this object works on.
	 */
	public static final String DEFAULT_SETTING_NAME = "dk.carecom.cts2.sync.settingfile";

	private static final int DEFAULT_RETRY_NUMBER = 3;
	
	/**
	 * Contains the loaded properties.
	 */
	private Properties properties;
	
	/**
	 * Private default constructor.
	 * @throws MissingSettingsFileSystemPropertyException If the system property specifying the location 
	 * of the settings file is not present.
	 * @throws SettingsFileNotAccessableException Thrown instead any IOException to allow lazy try-catch.
	 */
	private Settings() throws MissingSystemPropertyException, SettingsFileNotAccessableException {
		String path = System.getProperty(DEFAULT_SETTING_NAME);
		if (path == null) {
			throw new MissingSystemPropertyException(DEFAULT_SETTING_NAME);
		}
		File settingsFile = new File(path);
		
		this.properties = new Properties();
		try {
			this.properties.load(new FileReader(settingsFile));
		} catch (FileNotFoundException e) {
			throw new SettingsFileNotAccessableException("Settings file could not be found on the specified location", e);
		} catch (IOException e) {
			throw new SettingsFileNotAccessableException("Unhandled IOException was thrown", e);
		}
	}
	
	/**
	 * Returns the single unique instance of this class. This method is thread safe.
	 * @return A unique instance of a Settings class.
	 * @throws SettingsFileNotAccessableException Thrown instead any IOException to allow lazy try-catch..
	 * @throws MissingSettingsFileSystemPropertyException If the system property specifying the location 
	 * of the settings file is not present.
	 */
	public static Settings getInstance() throws MissingSystemPropertyException, SettingsFileNotAccessableException {
		if (uniqueInstance == null) {
			synchronized (Settings.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new Settings();
				}
			}
		}
		return uniqueInstance;
	}
	
	/**
	 * @return Web service URL pointing to the web service that will be called.
	 */
	public URL getWebServiceUrl() {
		try {
			return new URL(getSettingValue("webservice.url"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @return The ID of the user that has access to the web service.
	 */
	public String getWebServiceUser() {
		return getSettingValue("webservice.user");
	}
	
	/**
	 * @return The password of the user that grants access to the web service. 
	 */
	public String getWebServicePassword() {
		return getSettingValue("webservice.password");
	}
	
	/**
	 * @return The value of sync.longstrings setting.
	 */
	public String getSyncLongStrings() {
		return getSettingValue("sync.longstrings");
	}
	
	/**
	 * @return The value of sync.isdebug.
	 */
	public boolean getIsDebug() {
		String value = getSettingValue("sync.isdebug");
		return (value != null && value.equalsIgnoreCase("yes"));
	}
	
	/**
	 * @return The value of retry.number
	 */
	public int getRetryNumber() {
		try {
			return Integer.valueOf(getSettingValue("sync.retry.number"));
		} catch (Exception exception) {
			return DEFAULT_RETRY_NUMBER;
		}
	}

	/**
	 * @return TRUE if the settings indicate to truncate long strings.
	 */
	public boolean truncateLongStrings() {
		return VALUE_TRUNCATE.equalsIgnoreCase(getSyncLongStrings());
	}
	
	/**
	 * @return TRUE if the settings indicate to persist long strings.
	 */
	public boolean persistLongStrings() {
		// This is the default setting.
		return VALUE_PERSIST.equalsIgnoreCase(getSyncLongStrings()) || !truncateLongStrings();
	}
	
	/**
	 * @param key The name of the property to retrieve. 
	 * @return The value of the property that is retrieved.
	 */
	public String getSettingValue(String key) {
		if (key == null) {
			return "";
		}
		return (String)this.properties.get(key);
	}
}
