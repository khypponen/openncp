package eu.epsos.util.audit;

import java.io.Serializable;

public interface MessageHandlerListener {

	public boolean handleMessage(Serializable message);
	
}
