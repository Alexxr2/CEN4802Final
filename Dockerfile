FROM tomcat:11.0-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/cen4802final.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
