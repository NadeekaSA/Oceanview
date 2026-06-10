# Use Tomcat 10.1 with JDK 17 as the base image
FROM tomcat:10.1-jdk17-temurin

# Copy local dependency jars into Tomcat's lib directory
COPY gson-2.10.1.jar /usr/local/tomcat/lib/
COPY json-20251224.jar /usr/local/tomcat/lib/

# Download and add MySQL JDBC connector into Tomcat's lib directory
ADD https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.1.0/mysql-connector-j-9.1.0.jar /usr/local/tomcat/lib/

# Copy compiled WAR file to Tomcat's webapps directory as the ROOT application
COPY dist/Oceanview.war /usr/local/tomcat/webapps/ROOT.war

# Expose the default Tomcat port
EXPOSE 8080

# Run Tomcat Catalina server
CMD ["catalina.sh", "run"]
