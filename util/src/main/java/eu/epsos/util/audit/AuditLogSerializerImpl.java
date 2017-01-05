package eu.epsos.util.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogSerializerImpl implements AuditLogSerializer {

    private static final Logger log = LoggerFactory.getLogger(AuditLogSerializer.class);
    private static Type type;

    public AuditLogSerializerImpl(Type type) {
        AuditLogSerializerImpl.type = type;
    }

    public List<File> listFiles() {
        List<File> files = new ArrayList<>();
        File path = getPath();
        if (isPathValid(path)) {
            File[] srcfiles = path.listFiles();
            for (File file : srcfiles) {
                if (isAuditLogBackupWriterFile(file)) {
                    files.add(file);
                }
            }
        }

        return files;
    }

    public Serializable readObjectFromFile(File inFile) throws IOException, ClassNotFoundException {
        InputStream file = null;
        InputStream buffer = null;
        ObjectInput input = null;
        try {
            file = new FileInputStream(inFile);
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);
            return (Serializable) input.readObject();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.warn("Unable to close: " + e.getMessage(), e);
                }
            }
            close(buffer);
            close(file);
        }
    }

    public void writeObjectToFile(Serializable message) {
        OutputStream file = null;
        OutputStream buffer = null;
        ObjectOutput output = null;
        try {
            if (message != null) {
                String path = System.getenv("EPSOS_PROPS_PATH") + File.separatorChar + type.getNewFileName();

                file = new FileOutputStream(path);
                buffer = new BufferedOutputStream(file);
                output = new ObjectOutputStream(buffer);
                output.writeObject(message);

                log.error("Error occurred while writing AuditLog to OpenATNA! AuditLog saved to: " + path);
            }
        } catch (Exception e) {
            log.error("Unable to send AuditLog to OpenATNA nor able write auditLog backup! Dumping to log: " + message.toString(), e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    log.warn("Unable to close: " + e.getMessage(), e);
                }
            }
            close(buffer);
            close(file);
        }
    }

    private File getPath() {
        return new File(System.getenv("EPSOS_PROPS_PATH") + File.separatorChar + type.getDir());
    }

    private boolean isAuditLogBackupWriterFile(File file) {
        String fileName = file.getName();
        return fileName.startsWith(type.getFilePrefix()) && fileName.endsWith(type.getFileSuffix());
    }

    private boolean isPathValid(File path) {
        if (!path.exists()) {
            log.error("Source path (" + path + ") does not exist!");
            return false;
        } else if (!path.isDirectory()) {
            log.error("Source path (" + path + ") is not a diredtory!");
            return false;
        }

        return true;
    }

    private void close(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException e) {
            log.warn("Unable to close closeable: " + e.getMessage(), e);
        }
    }
}
