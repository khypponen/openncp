mvn clean
mvn package
mvn liferay:deploy
#scp target/portalb-1.1.0-SNAPSHOT.war administrator@192.168.1.219:/opt/openncp/liferay/deploy
