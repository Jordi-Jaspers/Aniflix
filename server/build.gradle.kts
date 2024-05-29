import io.spring.gradle.dependencymanagement.org.apache.commons.lang3.StringUtils.lowerCase
import org.cyclonedx.gradle.CycloneDxTask
import org.gradle.api.JavaVersion.VERSION_17
import org.gradle.api.file.DuplicatesStrategy.INCLUDE
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

// Both "group" and "version" are default Gradle properties, so they need to be set here
group = "org.jordijaspers"
version = "0.0.1-SNAPSHOT"

// Other, non-default Gradle, properties need to be defined here
val author = "Jordi Jaspers"
val github = "https://github.com/Jordi-Jaspers"

val applicationName = "aniflix"
val applicationDescription = "A backend system which consumes the 'consumet/api.consumet.org' api"
val applicationUrl = "https://aniflix.stream"

/**
 * Java 17 is long term supported and therefore chosen as the default.
 */
java {
    sourceCompatibility = VERSION_17
    targetCompatibility = VERSION_17
}

/**
 * The repositories used to download the dependencies.
 */
repositories {
    mavenCentral()
    mavenLocal()
}

/**
 * Project Plugins.
 */
plugins {
    // Java Management
    id("java")
    id("java-library")

    // adds intelliJ tasks to the build file and creates the settings in intellij correctly
    id("idea")

    // Spring boot
    id("org.springframework.boot") version "3.3.0"

    // A Gradle plugin that provides Maven-like dependency management functionality, which is used to set the versions of the dependencies.
    id("io.spring.dependency-management") version "1.1.5"

    // Quality plugin for Checkstyle, PMD and Spotbugs.
    id("ru.vyarus.quality") version "5.0.0"

    // The project-report plugin provides file reports on dependencies, tasks, etc.
    id("project-report")

    // Automatic lombok and delombok configuration.
    id("io.freefair.lombok") version "8.6"

    // SBOM generation for vulnerabilities
    id("org.cyclonedx.bom") version "1.8.2"

    // Automatically generates a list of updatable dependencies.
    id("com.github.ben-manes.versions") version "0.51.0"
}

/**
 * The dependencies of the project.
 */
dependencies {
    // Dependency versions
    val hawaiiVersion = "6.0.0.M11"
    val mapStructVersion = "1.6.0.Beta1"

    // ======= ANNOTATION PROCESSORS =======
    // annotation processor that generates metadata about classes in your application that are annotated with @ConfigurationProperties.
    annotationProcessor(group = "org.springframework.boot", name = "spring-boot-configuration-processor")

    // Provides Mapstruct annotations for spring.
    annotationProcessor(group = "org.mapstruct", name = "mapstruct-processor", version = mapStructVersion)

    // ======= RUNTIME DEPENDENCIES =======
    // Jdbc driver to connect with the MariaDB database.
    runtimeOnly(group = "org.mariadb.jdbc", name = "mariadb-java-client", version = "3.4.0")

    // ======= IMPLEMENTATION DEPENDENCIES ======= "
    // Spring Boot necessary dependencies.
    implementation(group = "org.springframework.boot", name = "spring-boot-starter")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-web")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-jdbc")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-webflux")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-mail")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-actuator")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-oauth2-resource-server")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-security")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-validation")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-thymeleaf")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-redis")

    // Open API documentation generation.
    implementation(group = "org.springdoc", name = "springdoc-openapi-starter-webmvc-ui", version = "2.5.0")

    // Adds logging for the hibernate queries.
    implementation(group = "com.github.gavlyukovskiy", name = "datasource-proxy-spring-boot-starter", version = "1.9.1")

    // Provides the core of hawaii framework such as the response entity exception handling.
    implementation(group = "org.hawaiiframework", name = "hawaii-starter-async", version = hawaiiVersion)
    implementation(group = "org.hawaiiframework", name = "hawaii-starter-rest", version = hawaiiVersion)

    // Mapstruct is used to generate code to map from domain model classes to rest application model classes
    implementation(group = "org.mapstruct", name = "mapstruct", version = mapStructVersion)

    // Library for checking that a password complies with a custom set of rules
    implementation(group = "org.passay", name = "passay", version = "1.6.4")

    // Used to validate entities and beans
    implementation(group = "jakarta.validation", name = "jakarta.validation-api", version = "3.1.0")
    implementation(group = "jakarta.servlet", name = "jakarta.servlet-api", version = "6.1.0-M2")

    // Mail service provider that supports thymeleaf templating.
    implementation(group = "jakarta.mail", name = "jakarta.mail-api", version = "2.1.3")

    // Reactive Jikan is a Java client for the Jikan API.
    implementation(group = "net.sandrohc", name = "reactive-jikan", version = "2.2.0")

    // Hibernate's core ORM functionality
    implementation(group = "org.hibernate", name = "hibernate-validator", version = "8.0.1.Final")
    implementation(group = "org.hibernate", name = "hibernate-core", version = "7.0.0.Alpha2")

    // Java utility classes for the classes that are in java.lang's hierarchy
    implementation(group = "org.apache.commons", name = "commons-lang3", version = "3.14.0")

    // Contain types that extend and augment the Java Collections Framework.
    implementation(group = "org.apache.commons", name = "commons-collections4", version = "4.5.0-M1")

    // Provide a datasource proxy that can inject your own logic into all queries.
    implementation(group = "net.ttddyy", name = "datasource-proxy", version = "1.10")

    // HTTPClient provides an efficient, up-to-date, and feature-rich package implementing the client side of the most recent HTTP standards and recommendations.
    implementation(group = "org.apache.httpcomponents", name = "httpclient", version = "4.5.14")

    // Apache Commons IO library contains utility classes, stream implementations, file filters, file comparators, endian transformation.
    implementation(group = "commons-io", name = "commons-io", version = "2.16.1")

    // Java library for Javascript Object Signing and Encryption (JOSE) and JSON Web Tokens (JWT)
    implementation(group = "com.nimbusds", name = "nimbus-jose-jwt", version = "9.39.2")

    // Hawaii-framework must-have logging dependencies.
    implementation(group = "org.slf4j", name = "jcl-over-slf4j", version = "2.1.0-alpha1")
    implementation(group = "net.logstash.logback", name = "logstash-logback-encoder", version = "7.4")
    implementation(group = "ch.qos.logback", name = "logback-access", version = "1.4.14")

    // ======= TEST DEPENDENCIES =======
    testImplementation(group = "org.springframework.boot", name = "spring-boot-starter-test")
    testImplementation(group = "org.springframework.security", name = "spring-security-test", version = "6.3.0")
}

// ============== PLUGIN CONFIGURATION ================
/**
 * Configuration propertied for the quality plugin.
 */
quality {
    autoRegistration = true
    configDir = "src/quality/"

    spotbugsVersion = "4.8.5"
    spotbugs = true

    pmdVersion = "6.55.0"
    pmd = true

    checkstyleVersion = "10.16.0"
    checkstyle = true

    codenarcVersion = "3.3.0"
    codenarc = true
}

// ============== TASK CONFIGURATION ================
tasks.getByName<BootJar>("bootJar") {
    duplicatesStrategy = INCLUDE
    archiveBaseName.set(lowerCase(applicationName))
    archiveVersion.set(project.version.toString())
    archiveFileName.set("${lowerCase(applicationName)}.jar")
    manifest {
        attributes("Name" to lowerCase(applicationName))
        attributes("Implementation-Title" to applicationDescription)
        attributes("Implementation-Vendor" to author)
        attributes("Implementation-Version" to project.version.toString())
    }
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.withType<JavaCompile> {
    options.isDeprecation = true
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(
        arrayOf(
            "-Xlint:all",
            "-Xlint:-processing",
            "-Amapstruct.defaultComponentModel=spring",
            "-Werror"
        )
    )
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(FAILED, PASSED, SKIPPED)
    }
}

tasks.named<DefaultTask>("build") {
    finalizedBy("dependencyUpdates")
}

tasks.withType<CycloneDxTask> {
    setProjectType("application")
    setSchemaVersion("1.5")
    setDestination(project.file("build/reports"))
    setOutputName("application-sbom")
    setOutputFormat("json")
    setIncludeBomSerialNumber(true)
    setIncludeLicenseText(true)
    setComponentVersion(version.toString())
}

tasks.named<BootRun>("bootRun") {
    val arguments = ArrayList<String>()
    if (project.hasProperty("debugPort")) {
        arguments.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=" + findProperty("debugPort"))
    }

    arguments.addAll(
        arrayOf(
            "-Xms512m",
            "-Xmx4096m",
            "-XX:MetaspaceSize=128m",
            "-XX:MaxMetaspaceSize=256m",
            "-XX:+UseG1GC",
            "-Djava.awt.headless=true"
        )
    )
    jvmArgs(arguments)
}
