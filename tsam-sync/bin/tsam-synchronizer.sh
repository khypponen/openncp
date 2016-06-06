#!/bin/sh

if [ -z "$JAVA_HOME" ]; then
    echo "the environment variable JAVA_HOME needs to be set and point to the java installation"
    exit 1
fi

cd `dirname $0`
TSAM_DIR=`dirname \`pwd\``
TSAM_JAR=epsos-tsam-sync-7.4.0-SNAPSHOT.jar

$JAVA_HOME/bin/java -Ddk.carecom.cts2.sync.hibernatefile=$TSAM_DIR/config/unix/hibernate.cfg.xml -Dlog4j.configuration=$TSAM_DIR/config/unix/log4j.xml -Djava.util.logging.config.file=$TSAM_DIR/config/unix/logging.properties -Ddk.carecom.cts2.sync.settingfile=$TSAM_DIR/config/unix/settings.properties -jar $TSAM_DIR/target/$TSAM_JAR


