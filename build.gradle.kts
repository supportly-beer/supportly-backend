import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "beer.supportly"
version = "0.0.1-SNAPSHOT"

plugins {
    id("org.springframework.boot") version "3.0.6"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.google.protobuf") version "0.9.3"

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

    implementation("com.google.zxing:core:3.5.1")
    implementation("com.google.zxing:javase:3.5.1")
    implementation("dev.turingcomplete:kotlin-onetimepassword:2.4.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")
    implementation("io.grpc:grpc-stub:1.54.1")
    implementation("io.grpc:grpc-protobuf:1.54.1")
    implementation("io.grpc:grpc-netty:1.54.1")
    implementation("com.google.protobuf:protobuf-java-util:3.22.3")
    implementation("com.google.protobuf:protobuf-kotlin:3.22.3")
    implementation("io.grpc:grpc-kotlin-stub:1.3.0")

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

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.22.3"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.54.1"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.3.0:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}