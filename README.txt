eHealth Project
===============
It has been created from OpenNCP projects and restructured to a multi module project. In order to build it just do the following:

Main Modules
------------
configuration-manager
e-sens-non-repudiation
security-manager
util
data-model
audit-manager
assertion-validator
cda-display-tool
default-policy-manager
trc-sts
trc-sts-client
tsam
tsam-sync
transformation-manager
eadc
consent-manager
tsl-utils
protocol-terminators
openatna
openncp-portal
openncp-common-components
openstork

Maven Configuration
-------------------
export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=350m"
mvn clean package install -DskipTests -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true

The ssl.insecure flags are for allowing IHE Gazelle endpoints needed for parsing ws endpoints

Please note that in order to use the IHE Nexus repository, you have to import the repository certificate into your
Maven configuration or into your JAVA_HOME/jre/lib/security/cacerts keystore (this certificate is updated frequently)
keytool -import -alias gazelle.nexus.ihe.net -file conformity.ihe.net.crt -keystore JAVA_HOME/jre/lib/security/cacerts

Tips for creating new versions
------------------------------
From the root directory just run the following command mvn versions:set -DnewVersion=<new version>
