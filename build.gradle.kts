plugins {
    kotlin("jvm") version "1.9.0"
    id("maven-publish")
    application
}

group = "cn.jjaw"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

java{
    withSourcesJar();
}

publishing{
    publications{
        create<MavenPublication>("maven-publish"){
            groupId = "cn.jjaw"
            artifactId = "easy-event"
            version = "1.0"
            from(components["java"])
        }
    }
}