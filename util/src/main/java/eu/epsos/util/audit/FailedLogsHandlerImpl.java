package eu.epsos.util.audit;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eu.epsos.util.audit.AuditLogSerializer.Type;

public class FailedLogsHandlerImpl implements FailedLogsHandler {
	private static Log log = LogFactory.getLog("org.openhealthtools.openatna.audit.service.FailedLogsHandlerImpl");
	private MessageHandlerListener listener;
	private AuditLogSerializer serializer;

	public FailedLogsHandlerImpl(MessageHandlerListener listener, Type type) {
		this.listener = listener;
		serializer = new AuditLogSerializerImpl(type);
	}

	public void run() {
		try {
			List<File> files = serializer.listFiles();
			for (File file : files) {
				log.info("Found file to be re-send to OpenATNAServer: " + file.getAbsolutePath());
				Serializable message = serializer.readObjectFromFile(file);
				if (listener.handleMessage(message)) {
					handleSentMessage(file);
				}

				try {
					Thread.sleep(1000);
				} catch(InterruptedException ie) {
					// Ignored
				}
			}
		} catch (Exception e) {
			log.fatal(e.getMessage(), e);
		}
	}

	private void handleSentMessage(File file) {
		if (!file.delete()) {
			log.error("Unable to delete successfully re-sent log backup (" + file.getAbsolutePath() + ")!");
		}
	}

}
