import org.gradle.api.tasks.testing.logging.TestLogEvent

/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version libs.versions.kotlin
    kotlin("plugin.parcelize")
    id("com.cmgapps.gradle.ktlint")
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "com.cmgapps.android.compose"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cmgapps.android.compose.playground"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        unitTests.all { test ->
            test.useJUnitPlatform()
            test.testLogging {
                events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.PASSED, TestLogEvent.STANDARD_OUT)
            }
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3.theme)
    implementation(libs.androidx.material3.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.adaptive.navigation.android)
    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.animation)
    implementation(libs.coil.compose)
    implementation(libs.androidx.palette)
    implementation(libs.reveal.core)
    implementation(libs.reveal.shapes)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
    testImplementation(libs.hamcrest)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}
