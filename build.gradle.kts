// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.4.0" apply false
    id("com.android.library") version "7.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.5" apply false
    kotlin("kapt") version "1.8.0"
}
//plugins {
//    id 'com.android.application' version '7.4.0' apply false
//    id 'com.android.library' version '7.4.0' apply false
//    id 'org.jetbrains.kotlin.android' version '1.8.0' apply false

//apply("${project.rootDir}/buildscripts/toml-updater-config.gradle")