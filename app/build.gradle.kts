plugins {
    application
    id("convention-kotlin")
    id("convention-style")
    id("convention-test")
    kotlin("plugin.serialization") version "1.9.24"
}

repositories {
    // Kord
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    // serialization
    implementation(libs.kotlin.serialization)

    // di
    implementation(libs.koin)

    // discord
    implementation(libs.kord.core)
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

    // logging
    implementation(libs.kermit)
}

application {
    mainClass.set("com.elliegabel.sunnybot.AppKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.elliegabel.sunnybot.AppKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
