//import com.android.build.api.dsl.Packaging
//import org.jetbrains.kotlin.config.KotlinCompilerVersion
@file:Suppress("UnstableApiUsage")

import com.android.build.api.variant.BuildConfigField

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 33
    namespace = "com.vijanthi.computervathiyar"
    defaultConfig {
        applicationId = "com.vijanthi.computervathiyar"
        minSdk = 25
        targetSdk = 33
        versionCode = 3
        versionName = "1.0.0.1"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    viewBinding.enable = true
    buildFeatures {
        compose = true
    }


//    @Suppress("UNUSED_EXPRESSION")
//    fun Packaging.() {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
    configurations.all {
        resolutionStrategy.force("androidx.compose.animation:animation:1.4.0")
    }
    androidComponents {
        onVariants {
            it.buildConfigFields.put(
                "BASE_URL", BuildConfigField(
                    "String", "\"http://192.168.1.1:8080/\"", "build timestamp"
                )
            )
        }
    }

    kapt {
        correctErrorTypes = true
    }

    signingConfigs {
        create("computervathiyar") {
            storeFile = File("/mnt/work/Projects/Netstar/SerialComm/ComputerVathiyar/app/keystore/computer-vathiyar.jks")
            storePassword = "vathiyar"
            keyPassword = "vathiyar"
            keyAlias = "ComputerVathiyar"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            signingConfig = signingConfigs.getByName("computervathiyar")
        }
        debug {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            signingConfig = signingConfigs.getByName("computervathiyar")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.1")

    implementation(platform("androidx.compose:compose-bom:2023.05.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // tradional xml
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

//    hilt
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-compiler:2.46.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0")
    implementation("com.airbnb.android:lottie-compose:6.0.0")
//    editor js
    implementation("io.github.upstarts:ejkit:2.0.0")
    implementation("io.github.upstarts:ejkit-gson:2.0.0")
    implementation("io.github.upstarts:ejkit-moshi:2.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    implementation("com.google.accompanist:accompanist-navigation-animation:0.30.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")// For Hilt"s nav-scoped VMs

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")
    //gms
    implementation("com.google.android.gms:play-services-auth:20.5.0")
    // ads
    implementation("com.google.android.gms:play-services-ads:22.1.0")
    // firebase
    implementation(platform("com.google.firebase:firebase-bom:32.1.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.5.0")
    implementation("com.google.firebase:firebase-analytics-ktx")

    implementation("androidx.core:core-splashscreen:1.0.1")

    val room_version = "2.5.1"
//    val room_version = "2.6.0-alpha01"
//    kapt("androidx.room:room-compiler:$room_version")]
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    implementation("io.coil-kt:coil-compose:2.2.2")

    // Accompanist libraries
    implementation("com.google.accompanist:accompanist-swiperefresh:0.31.2-alpha")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    androidTestImplementation(platform("androidx.compose:compose-bom:2023.05.01"))

    debugImplementation("androidx.compose.ui:ui-tooling:1.4.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.3")
}