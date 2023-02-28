import org.jetbrains.kotlin.ir.backend.js.compile

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
//    flatDir {
//        dirs("/home/alex/Mephi/sem3/Kotlin/projects/youtube_project/youtube_whole/youtube3")
//    }

}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.rabbitmq:micronaut-rabbitmq")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")

    api(project(":common"))
//    api(file("common"))
//    compile(file("../src/strings"))
//    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")


    //for database
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.sql:micronaut-hibernate-jpa")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("jakarta.persistence:jakarta.persistence-api")


    //transcoder
    implementation("ws.schild:jave-core:3.3.1")
    implementation("ws.schild:jave-nativebin-linux64:3.3.1")


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
//        sharedServer.set(true)
//    }
}



