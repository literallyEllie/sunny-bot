plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "sunny-bot"
includeBuild("build-logic")
include("app")
