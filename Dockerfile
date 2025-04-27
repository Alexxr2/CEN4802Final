FROM tomcat:11.0-jdk17
# Remove default webapps
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy your WAR into ROOT.war
COPY target/cen4802final.war /usr/local/tomcat/webapps/ROOT.war
# Expose port 8080
EXPOSE 8080
