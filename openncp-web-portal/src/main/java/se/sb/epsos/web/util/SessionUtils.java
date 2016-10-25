package se.sb.epsos.web.util;

import java.io.Serializable;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;

public class SessionUtils {

	public static void setSessionMetaData(MetaDataKey<? extends Serializable> key, Serializable value) {
		Session.get().setMetaData(key, value);
	}
	
	public static <T extends Serializable> T getSessionMetaData(MetaDataKey<T> key) {
		return Session.get().getMetaData(key);
	}
}
