package eu.epsos.log4j.appender;

import eu.epsos.log4j.util.ThrowableUtil;
import org.slf4j.event.LoggingEvent;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class DatabaseAppender {/*extends AppenderSkeleton implements Appender {
    private Connection connection;

    @SuppressWarnings({"FieldCanBeLocal"})
    private String user;
    private String password;
    private String url;

    private InetAddress inetAddress;

    private final BlockingQueue<LoggingEvent> loggingEventQueue
            = new LinkedBlockingDeque<>();

    public DatabaseAppender() {
        startInsertThread();
    }

    @Override
    protected void append(LoggingEvent event) {
        loggingEventQueue.add(event);
    }

    private void execute(PreparedStatement statement) throws SQLException {
        try {
            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    private Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = newConnection();
        }

        return connection;
    }

    public void setDriver(String driverClass) {
        try {
            Class.forName(driverClass);
        } catch (Exception e) {
            errorHandler.error("Failed to load driver", e,
                    ErrorCode.GENERIC_FAILURE);
        }
    }


    private Connection newConnection() throws SQLException {
        if (!DriverManager.getDrivers().hasMoreElements())
            setDriver("sun.jdbc.odbc.JdbcOdbcDriver");

        if (connection == null) {
            connection = DriverManager.getConnection(url, user, password);
        }

        return connection;
    }

    private PreparedStatement getLogStatement(LoggingEvent event) throws SQLException {
        String pattern = ((PatternLayout) getLayout()).getConversionPattern();

        StringBuilder sb = new StringBuilder();
        List<String> patterns = new ArrayList<String>();

        for (int i = 0; i < pattern.length(); i++) {
            if (i + 1 < pattern.length() && pattern.charAt(i) == '?' && pattern.charAt(i + 1) == '{') {
                sb.append('?');
                int last = i + 2;
                int balance = 1;
                while (last < pattern.length() && balance != 0) {
                    if (pattern.charAt(last) == '{') {
                        balance++;
                    }
                    if (pattern.charAt(last) == '}') {
                        balance--;
                    }
                    last++;
                }
                patterns.add(pattern.substring(i + 2, last - 1));
                i = last - 1;
            } else {
                sb.append(pattern.charAt(i));
            }
        }

        PreparedStatement statement = getConnection().prepareStatement(sb.toString());

        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i).equals("stacktrace")) {
                if (event.getThrowableInformation() != null) {
                    statement.setString(i + 1, ThrowableUtil.getStacktrace(event.getThrowableInformation().getThrowable()));
                } else {
                    statement.setString(i + 1, null);
                }
                continue;
            }

            if (patterns.get(i).equals("ip")) {
                statement.setString(i + 1, (inetAddress == null ? "0.0.0.0" : inetAddress.getHostAddress()));
                continue;
            }

            PatternLayout patternLayout = new PatternLayout("%" + patterns.get(i));
            statement.setString(i + 1, patternLayout.format(event));
        }

        return statement;
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setSql(String sql) {
        if (getLayout() == null) {
            this.setLayout(new PatternLayout(sql));
        } else {
            ((PatternLayout) getLayout()).setConversionPattern(sql);
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public void finalize() {
        close();
        super.finalize();
    }

    private void processEvent(LoggingEvent loggingEvent) {
        try {
            PreparedStatement sql = getLogStatement(loggingEvent);
            execute(sql);
        }
        catch (SQLException e) {
            errorHandler.error("Failed to execute sql", e, ErrorCode.FLUSH_FAILURE);
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            errorHandler.error("Error closing connection", e, ErrorCode.GENERIC_FAILURE);
        }

        this.closed = true;
    }

    @SuppressWarnings({"InfiniteLoopStatement"})
    private void processQueue() {
        while (true) {
            try {
                LoggingEvent event = loggingEventQueue.poll(1L, TimeUnit.SECONDS);
                if (event != null) {
                    processEvent(event);
                }
            } catch (InterruptedException e) {
                // No operations.
            }
        }
    }

    private void startInsertThread() {
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            inetAddress = null;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                processQueue();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }*/
}
