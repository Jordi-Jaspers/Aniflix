ARG SMT_REGISTRY="registry.smtnexus.internal.vodafone.nl/"
FROM ${SMT_REGISTRY}ibm-semeru-runtimes:open-17.0.6_10-jdk-jammy AS create-slimjre

RUN mkdir /workspace
WORKDIR /workspace
ADD build/libs/shx.jar /workspace/shx.jar

# Create a minimal JRE for this application
RUN jlink --no-header-files --no-man-pages --compress=2 --strip-java-debug-attributes --add-modules java.base,java.desktop,jdk.crypto.cryptoki,java.xml,jdk.unsupported,java.sql,java.naming,java.logging,java.management,java.security.jgss,java.instrument,openj9.sharedclasses,jdk.zipfs,jdk.attach --output slimjre

# Add all the root CAs in the cacerts directory in the temporary container
RUN mkdir /workspace/cacerts
COPY cacerts /workspace/cacerts

# Copy the script, and then run it to add the CAs to the default Java truststore
COPY add-root-cas.sh /workspace/add-root-cas.sh
RUN chmod 755 /workspace/add-root-cas.sh
RUN /workspace/add-root-cas.sh

# Extract the JAR file so it can be run directly from disk
RUN mkdir unpacked; cd unpacked; jar -xf /workspace/shx.jar

# Base this image on top of an empty Ubuntu container, since we have created our own JRE
FROM ${SMT_REGISTRY}ubuntu:jammy

COPY vodafone-internal-ca.crt /etc/ssl/certs/ca-certificates.crt
COPY vodafone-internal-ca.crt /usr/local/share/ca-certificates/vodafone-internal-ca.crt
COPY sources.list /etc/apt/sources.list

RUN export DEBIAN_FRONTEND=noninteractive &&  \
    apt-get update && \
    apt-get install -y --no-install-recommends vim curl psmisc net-tools apt-utils tzdata ca-certificates fontconfig libfontconfig1 libfreetype6 locales apt-utils tzdata uuid tcpdump jq libtcnative-1 curl ca-certificates && \
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
# Create user and group to run the application with and set correct permissions to our base directory
    groupadd -g 10001 shx && useradd -m -d /opt/hawaii -g shx -u 10000 shx && \
# Create the necessary directories as the correct user
    install -d -o shx -g shx -m 770 /opt/hawaii/app /opt/hawaii/config

# Switch to our working directory
WORKDIR /opt/hawaii

# Install the slim JRE in the container
COPY --from=create-slimjre --chown=shx:shx /workspace/slimjre /opt/hawaii/slimjre/

# Install liveness.sh script
COPY --chown=shx:shx liveness.sh /opt/hawaii/liveness.sh
RUN chmod +x /opt/hawaii/liveness.sh

# Container health check, using the script above
HEALTHCHECK --interval=30s --timeout=5s --retries=3 CMD /opt/hawaii/liveness.sh

# Default environment variables, these can be overwritten when calling "docker run"
ENV JAVA_TOOL_OPTIONS="-XX:+IgnoreUnrecognizedVMOptions -XX:+IdleTuningCompactOnIdle -XX:+IdleTuningGcOnIdle -XX:-HeapDumpOnOutOfMemoryError" \
    RAM_PERCENTAGE_MIN="5" \
    RAM_PERCENTAGE_MAX="80" \
    METASPACE_SIZE_MIN="32m" \
    METASPACE_SIZE_MAX="128m" \
    SPRING_PROFILES="dev" \
    LOG_APPENDER="LOGSTASHENCODER" \
    JMX_PORT="8079" \
    ASYNC_CONFIG="/opt/hawaii/config/async-config.yml" \
    TZ="Europe/Amsterdam" \
    ELASTIC_APM_SERVICE_NAME="Statehistory" \
    ELASTIC_APM_ENABLED="false" \
    ELASTIC_APM_APPLICATION_PACKAGES="nl.vodafoneziggo.smc.shx" \
    ELASTIC_APM_ENVIRONMENT="DEV" \
    ELASTIC_APM_SERVER_URL="https://smtapmehv.internal.vodafone.nl/"


# Expose the HTTP, AJP, and JMX ports
EXPOSE 8080 8009 8079

# Install the Spring boot config files in the container
COPY --chown=shx:shx config /opt/hawaii/config/

# Install the unpacked Spring Boot application in the container
COPY --from=create-slimjre --chown=shx:shx /workspace/unpacked/BOOT-INF/lib /opt/hawaii/app/lib
COPY --from=create-slimjre --chown=shx:shx /workspace/unpacked/META-INF /opt/hawaii/app/META-INF
COPY --from=create-slimjre --chown=shx:shx /workspace/unpacked/BOOT-INF/classes /opt/hawaii/app

# From now on, run everything as non-root user
USER shx

# Warm the share classes cache to have faster startups by starting the application once at build-time using the jenkins profile
# This will start the application using the build-time parameters
# Wait for the startup to complete (i.e. part 8080 starts listening) and then kill the java application to finish the build
RUN /bin/sh -c '/opt/hawaii/slimjre/bin/java -Xshareclasses -Xscmx45M -XX:+UseContainerSupport -XX:InitialRAMPercentage=${RAM_PERCENTAGE_MIN} -XX:MaxRAMPercentage=${RAM_PERCENTAGE_MAX} -XX:MetaspaceSize=${METASPACE_SIZE_MIN} -XX:MaxMetaspaceSize=${METASPACE_SIZE_MAX} -XX:+AlwaysPreTouch -XX:TieredStopAtLevel=1 -XX:+UseG1GC -XX:+CMSClassUnloadingEnabled -Djava.security.egd=file:/dev/urandom -Djava.awt.headless=true -Dspring.devtools.restart.enabled=false -Dcom.sun.management.jmxremote.port=${JMX_PORT} -Dcom.sun.management.jmxremote.rmi.port=${JMX_PORT} -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.local.only=true -Dcom.sun.management.jmxremote.host=localhost -Djava.rmi.server.hostname=127.0.0.1 -Djava.library.path=/opt/hawaii/slimjre/lib/default:/opt/hawaii/slimjre/lib:/usr/lib -cp /opt/hawaii/app:/opt/hawaii/app/lib/* nl.vodafoneziggo.smc.shx.Shx --spring.config.additional-location=/opt/hawaii/config/ --spring.profiles.include=jenkins --spring.output.ansi.enabled=always &' ; let limit=120; let num=0; let break=0; let listen=0; while [ $break -lt 1 ]; do let num=$num+1; listen=`netstat -an | grep LISTEN | grep "0.0.0.0:8080" | wc -l`; if [ $listen -eq 1 -o $num -ge $limit ]; then let break=1; fi; echo "Try ${num}: Waiting for port 8080 to come up..."; sleep 1; done; echo "Port 8080 is now up, killing java..."; killall java || true

# Start the Spring Boot process
CMD /opt/hawaii/slimjre/bin/java \
# Enable OpenJ9 class share cache for faster startup
-Xshareclasses -Xscmx45M \
# Java heap size using container support
-XX:+UseContainerSupport \
-XX:InitialRAMPercentage=${RAM_PERCENTAGE_MIN} -XX:MaxRAMPercentage=${RAM_PERCENTAGE_MAX} \
-XX:MetaspaceSize=${METASPACE_SIZE_MIN} -XX:MaxMetaspaceSize=${METASPACE_SIZE_MAX} \
# JDK settings
-XX:+AlwaysPreTouch \
-XX:TieredStopAtLevel=1 \
-XX:+UseG1GC \
-XX:+CMSClassUnloadingEnabled \
# Java system properties
-Djava.security.egd=file:/dev/urandom -Djava.awt.headless=true -Dspring.devtools.restart.enabled=false \
# Enable JMX on localhost for Metricbeat
-Dcom.sun.management.jmxremote.port=${JMX_PORT} -Dcom.sun.management.jmxremote.rmi.port=${JMX_PORT} -Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.local.only=true -Dcom.sun.management.jmxremote.host=localhost -Djava.rmi.server.hostname=127.0.0.1 \
# Add the Tomcat native library directory
-Djava.library.path=/opt/hawaii/slimjre/lib/default:/opt/hawaii/slimjre/lib:/usr/lib \
# Set Elastic APM status
-Delastic.apm.enabled=${ELASTIC_APM_ENABLED} \
# Bootable jar file
-cp /opt/hawaii/app:/opt/hawaii/app/lib/* nl.vodafoneziggo.smc.shx.Shx \
# Spring boot properties
--spring.config.additional-location=/opt/hawaii/config/ \
--spring.profiles.include=${SPRING_PROFILES} \
--spring.output.ansi.enabled=always
