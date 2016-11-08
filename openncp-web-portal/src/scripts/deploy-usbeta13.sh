#!/bin/sh
echo Copying artifact $4 to remote server
scp -i $1 $3/$4 $2@95.131.200.23:/opt/spirit/deploymentstage/$4
echo Stopping applicaiton
ssh -i $1 $2@95.131.200.23 '/opt/spirit/sbin/eps-stop portal'
sleep 10
echo Removing old deployment
ssh -i $1 $2@95.131.200.23 'rm -Rf /opt/spirit/tomcat6-epsosweb/webapps/epsos-web*'
echo Deploying the new version
ssh -i $1 $2@95.131.200.23 /opt/spirit/sbin/copyFileWithGroup spirit /opt/spirit/deploymentstage/$4 /opt/spirit/tomcat6-epsosweb/webapps/epsos-web.war
sleep 5
echo Starting application
ssh -i $1 $2@95.131.200.23 '/opt/spirit/sbin/eps-start portal'
echo Cleaning up deployment stage, removing $4
ssh -i $1 $2@95.131.200.23 rm /opt/spirit/deploymentstage/$4

