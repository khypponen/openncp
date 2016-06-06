eHealth Project
It has been created from OpenNCP projects and restructured to a multi module project. In order to build it just do the following:

Maven Configuration
export MAVEN_OPTS="-Xmx512m -XX:MaxPermSize=350m"
mvn clean package install -DskipTests -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true  

The ssl.insecure flags are for allowing IHE Gazelle endpoints needed for parsing ws endpoints

