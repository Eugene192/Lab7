plugins {
    id("java")
    application
}

group = "org.eugene"
version = "unspecified"

repositories {
    mavenCentral()
}

application {
    mainClass.set("org.eugene.client.Main")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(project(":common"))
    implementation("com.jcraft:jsch:0.1.44-1")
}

tasks {
    val fatJar = register<org.gradle.jvm.tasks.Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("fat") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}
