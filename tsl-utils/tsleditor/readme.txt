Prerequisites for compiling this project
========================================

One of the dependencies for this project is
http://code.google.com/p/eid-tsl/ As the library is not available on
any maven repositories, you have to either manually install the
library to your local maven repository:

mvn install:install-file -Dfile=eid-tsl-core-1.0.0-SNAPSHOT.jar -DgroupId=be.fedict.eid -DartifactId=eid-tsl-core -Dversion=1.0.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=zehon_file_transfer-1.1.6.jar -DgroupId=com.zehon -DartifactId=zehon_file_transfer -Dversion=1.1.6 -Dpackaging=jar

Running the TSLEditor
=====================

In order to run just point to the target and run java -jar
TSLEditor_Version.jar. If you want to copy the executable don't forget
to get also the lib folder.
