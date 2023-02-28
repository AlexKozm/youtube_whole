plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.7.2"
//    id("io.micronaut.test-resources") version "3.7.2"
}

version = "0.1"
group = "com.example"

val kotlinVersion = project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.rabbitmq:micronaut-rabbitmq")
    implementation("io.micronaut:micronaut-validation")

    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    //logger
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("ch.qos.logback:logback-core")
    implementation("org.slf4j:slf4j-api")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("io.projectreactor:reactor-core:3.4.16")
//    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")
    implementation("com.github.reactivex:rxkotlin:3.0.1")

    implementation("com.google.guava:guava:31.0.1-jre")
    api(project(":common"))


    //-----
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.sql:micronaut-hibernate-jpa")

    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("io.micronaut.data:micronaut-data-model:3.9.6")
    implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")
    annotationProcessor("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.flyway:micronaut-flyway")
    runtimeOnly("org.flywaydb:flyway-mysql")


//    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")


}


application {
    mainClass.set("com.example.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}
graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
//    testResources {
//        additionalModules.add("jdbc-mariadb")
//        sharedServer.set(true)
//    }
}



