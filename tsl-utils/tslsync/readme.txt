Create a single jar to run it on demand: mvn clean compile assembly:single
Create a war file to run it inside tomcat as a war file

mvn install:install-file -Dfile=epsos-tsl-sync-2.8.8.jar -DgroupId=eu.europa.ec.joinup.ecc.epsos-tslutils -Dversion=2.8.8 -DartifactId=epsos-tsl-sync -Dpackaging=jar