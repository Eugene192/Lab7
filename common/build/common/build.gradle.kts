plugins {
    id("java")
}

group = "org.eugene"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("commons-cli:commons-cli:1.7.0")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
}