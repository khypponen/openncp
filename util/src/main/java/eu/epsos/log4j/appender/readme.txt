An example of log4j:

log4j.rootCategory=INFO,database 

log4j.appender.database.driver=com.mysql.jdbc.Driver
log4j.appender.database=eu.epsos.log4j.appender.DatabaseAppender
log4j.appender.database.URL=jdbc:mysql://localhost:3306/logs
log4j.appender.database.user=???
log4j.appender.database.password=???
log4j.appender.database.sql=INSERT INTO LogEntry (component, creationTime, logger, \
priority, message, stacktrace, ip) VALUES ('ComponentName', \
?{d{yyyy-MM-dd HH:mm:ss}}, ?{c}, ?{p}, ?{m}, ?{stacktrace}, ?{ip})
log4j.appender.database.layout=org.apache.log4j.PatternLayout
log4j.appender.database.layout.ConversionPattern=%-5p [%d{yyyy-MM-dd HH:mm:ss}] %C{1}: %m (%F:%L, %t)%n
The parts of log4j.appender.database.sql like ?{p} will be translated into prepared statement parameters (?), which will be assigned from PatternLayout correspondent value (like %p). As since prepared statement used there will be no problems with quotation or other special chars.

There are special tokens ?{stacktrace} (which will be assigned from exception stacktrace or NULL) and ?{ip} (which will be assigned from IP address}.

Database scheme (mysql):

CREATE TABLE IF NOT EXISTS `LogEntry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `component` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `logger` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `priority` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `message` longtext COLLATE utf8_unicode_ci NOT NULL,
  `stacktrace` text COLLATE utf8_unicode_ci,
  `creationTime` datetime NOT NULL,
  `ip` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `priority` (`priority`),
  KEY `creationTime` (`creationTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;