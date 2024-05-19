buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

plugins {
    id("convention-test")
}

group = "com.elliegabel"
version = "0.0.2"

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}