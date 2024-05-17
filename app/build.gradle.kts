plugins {
    application
    `kotlin-dsl`
    `java-library`
    id("convention-kotlin")
    id("convention-style")
    id("convention-test")
}

repositories {
    // Discord
    // Kord
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    // extensions
//    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    // di
    implementation(libs.koin)

    // discord
    implementation(libs.kord.core)
//    implementation(libs.kord.extentions)

    implementation(libs.kermit)

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.elliegabel.sunnybot.AppKt")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}