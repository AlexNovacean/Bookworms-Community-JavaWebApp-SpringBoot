# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="novacean.alex@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 9090

# The application's jar file
ARG JAR_FILE=target/bookwormscommunity-1.0-RELEASE.jar

# Add the application's jar to the container
ADD ${JAR_FILE} bookwormscommunity.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "bookwormscommunity.jar"]
