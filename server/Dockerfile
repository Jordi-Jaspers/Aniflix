# STEP 1: Create a builder image
# The first step depends on a create build image first by executing the following command: `./gradlew clean build`.
FROM eclipse-temurin:17-jdk-jammy AS create-slimjre

LABEL version="0.0.1-SNAPSHOT"
LABEL description="Aniflix Server Docker Image"
LABEL maintainer="Jordi Jaspers"

# Copy the generated jar file in a temporary directory
RUN mkdir /workspace
WORKDIR /workspace
ADD build/libs/aniflix.jar /workspace/aniflix.jar

# Create a minimal JRE for this application
RUN jlink --no-header-files --no-man-pages --compress=2 --strip-java-debug-attributes --add-modules java.base,java.desktop,jdk.crypto.cryptoki,java.xml,jdk.unsupported,java.sql,java.naming,java.logging,java.management,java.security.jgss,java.instrument,jdk.zipfs,jdk.attach --output slimjre

# Extract the JAR file so it can be run directly from disk
RUN mkdir unpacked; cd unpacked; jar -xf /workspace/aniflix.jar

# STEP 2: Create a smaller runtime image for the application
FROM ubuntu:jammy

# Setup the Ubuntu environment
RUN export DEBIAN_FRONTEND=noninteractive &&  \
    # Update the package list
    apt-get update && \
    # Install some handy tools and necessary dependencies
    apt-get install -y --no-install-recommends apt-utils tzdata fontconfig locales uuid tcpdump jq libtcnative-1 vim wget psmisc net-tools curl ca-certificates && \
    echo "en_US.UTF-8 UTF-8" >> /etc/locale.gen && \
    locale-gen en_US.UTF-8 && \
    # Upgrade everything in the image for security reasons
    apt-get dist-upgrade -y && \
    # Make sure bash is used as default shell, instead of dash
    echo "dash dash/sh boolean false" | debconf-set-selections && \
    dpkg-reconfigure dash && \
    # Clean up
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    # Create the necessary user and directories \
    groupadd -g 10001 aniflix && useradd --create-home -u 10000 -g 10001 aniflix && \
    install -d -m 770 -o aniflix -g aniflix /opt/workspace/app /opt/workspace/config

# Set ownership of /opt/workspace to aniflix, because the application writes files there.
RUN chown aniflix:aniflix /opt/workspace

# From now on, run everything as non-root user
USER aniflix

# Switch to our working directory
WORKDIR /opt/workspace

# Install the slim JRE in the container
COPY --from=create-slimjre --chown=aniflix:aniflix /workspace/slimjre /opt/workspace/slimjre/

# These are build arguments that will be set by Jenkins at build time to the running Inbucket container, but
# they will also be added to the environment list below, so they can be overridden at run time in Kubernetes/Docker
ARG SPRING_PROFILES_INCLUDE="development"

# Default environment variables, these can be overwritten when calling "docker run"
ENV SPRING_PROFILES_INCLUDE=$SPRING_PROFILES_INCLUDE \
    TZ="Europe/Amsterdam" \
    JAVA_TOOL_OPTIONS="-XX:+IgnoreUnrecognizedVMOptions -XX:+UseContainerSupport -XX:+IdleTuningCompactOnIdle -XX:+IdleTuningGcOnIdle" \
    HEAP_SIZE_MIN="64m" \
    HEAP_SIZE="512m" \
    METASPACE_SIZE_MIN="32m" \
    METASPACE_SIZE="128m" \
    SHARE_CLASSES_SIZE="128m" \
    SHARE_CLASSES_AOT_SIZE="100m"

# Container health check
HEALTHCHECK --interval=30s --timeout=5s --retries=3 CMD /bin/sh -c "/usr/bin/wget -Y off -nv -t1 --spider -O - http://localhost:8080/aniflix/actuator/health || exit 1"

# Expose ports 8080 for HTTP and 8009 for AJP
EXPOSE 8080 8009

# Install the Spring boot config files in the container
RUN mkdir -p /opt/workspace/config
COPY --chown=aniflix:aniflix config /opt/workspace/config

# Install the unpacked Spring Boot application in the container
COPY --from=create-slimjre --chown=aniflix:aniflix /workspace/unpacked/BOOT-INF/lib /opt/workspace/app/lib
COPY --from=create-slimjre --chown=aniflix:aniflix /workspace/unpacked/META-INF /opt/workspace/app/META-INF
COPY --from=create-slimjre --chown=aniflix:aniflix /workspace/unpacked/BOOT-INF/classes /opt/workspace/app

# Start the Spring Boot process
CMD /opt/workspace/slimjre/bin/java \
# Enable OpenJ9 class share cache for faster startup
-Xshareclasses -Xscmx${SHARE_CLASSES_SIZE} -Xscmaxaot${SHARE_CLASSES_AOT_SIZE} \
# Java heap size
-Xms${HEAP_SIZE_MIN} -Xmx${HEAP_SIZE} \
# Java metaspace size
-XX:MetaspaceSize=${METASPACE_SIZE_MIN} -XX:MaxMetaspaceSize=${METASPACE_SIZE} \
# Garbage collection flags
-XX:+AlwaysPreTouch \
-XX:TieredStopAtLevel=1 \
-XX:+UseG1GC \
-XX:+CMSClassUnloadingEnabled \
# Java system properties
-Djava.security.egd=file:/dev/urandom -Djava.awt.headless=true -Dspring.devtools.restart.enabled=false \
# Add tomcat native library to java.library.path
-Djava.library.path=/opt/workspace/slimjre/lib/default:/opt/workspace/slimjre/lib:/usr/lib64:/usr/lib:/usr/lib/x86_64-linux-gnu \
# Bootable jar file
-cp /opt/workspace/app:/opt/workspace/app/lib/* org.jordijaspers.aniflix.Aniflix \
# Spring boot properties
--spring.config.additional-location=/opt/workspace/config/ \
--spring.output.ansi.enabled=always \
--spring.profiles.include=${SPRING_PROFILES_INCLUDE}
