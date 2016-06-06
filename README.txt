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
epsos-common-components
OPENSTORK

Maven Configuration
-------------------
export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=350m"
mvn clean package install -DskipTests -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true  

The ssl.insecure flags are for allowing IHE Gazelle endpoints needed for parsing ws endpoints

Tips for creating new versions
------------------------------
From the root directory just run the following command mvn versions:set -DnewVersion=<new version>


