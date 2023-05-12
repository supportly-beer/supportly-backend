import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "beer.supportly"
version = "0.0.1-SNAPSHOT"

plugins {
    id("org.springframework.boot") version "3.0.6"
    id("io.spring.dependency-management") version "1.1.0"

    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation(group = "io.jsonwebtoken", name = "jjwt-api", version = "0.11.5")
    implementation(group = "io.jsonwebtoken", name = "jjwt-impl", version = "0.11.5")
    implementation(group = "io.jsonwebtoken", name = "jjwt-jackson", version = "0.11.5")

    runtimeOnly("com.mysql:mysql-connector-j:8.0.33")
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}