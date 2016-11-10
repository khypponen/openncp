ECHO OFF
ECHO Copying artifact %4 to remote server
scp -i %1 %3/%4 %2@95.131.200.23:/opt/spirit/deploymentstage/%4
ECHO Deploying the new version
ssh -i %1 %2@95.131.200.23 "/opt/spirit/sbin/copyFileWithGroup spirit /opt/spirit/deploymentstage/%4 /opt/spirit/tomcat6-epsosweb/webapps/epsos-web.war"
ECHO Cleaning up deployment stage, removing %4
ssh -i %1 %2@95.131.200.23 "rm /opt/spirit/deploymentstage/%4"

