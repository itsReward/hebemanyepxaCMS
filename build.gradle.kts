import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
}

group = "com.hebe"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.webjars:bootstrap:5.3.0")
    implementation("org.webjars:jquery:3.6.4")
    implementation("org.webjars:font-awesome:6.4.0")
    implementation("org.flywaydb:flyway-core")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    // Add Jsoup for HTML parsing
    implementation("org.jsoup:jsoup:1.16.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Make sure bootJar task is explicitly configured
tasks.bootJar {
    archiveFileName.set("app.jar")

    // Print information about the output
    doLast {
        println("====== Boot JAR created ======")
        println("JAR file: ${archiveFile.get().asFile.absolutePath}")
        println("JAR file exists: ${archiveFile.get().asFile.exists()}")
        println("JAR file size: ${archiveFile.get().asFile.length()} bytes")
        println("==============================")
    }
}

// Explicitly disable the plain jar task to avoid confusion
tasks.jar {
    enabled = false
}

// Add a task to print build info
tasks.register("printBuildInfo") {
    doLast {
        println("====== Build Information ======")
        println("Project: ${project.name}")
        println("Group: ${project.group}")
        println("Version: ${project.version}")
        println("Build directory: ${project.buildDir}")
        println("==============================")
    }
}

// Make bootJar depend on printBuildInfo
tasks.bootJar {
    dependsOn("printBuildInfo")
}