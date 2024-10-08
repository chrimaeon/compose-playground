/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import org.gradle.api.tasks.testing.logging.TestLogEvent
import java.time.LocalDate

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version libs.versions.kotlin
    kotlin("plugin.parcelize")
    alias(libs.plugins.ksp)
    id("com.cmgapps.gradle.ktlint")
    alias(libs.plugins.paparazzi)
    id("licenses")
}

android {
    namespace = "com.cmgapps.android.compose"

    buildToolsVersion = libs.versions.buildTools.get()
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.cmgapps.android.compose.playground"
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resourceConfigurations += listOf("en")
    }

    buildTypes {
        debug {
            buildConfigField("String", "BUILD_YEAR", """"TBD"""")
        }
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildConfigField("String", "BUILD_YEAR", """"${LocalDate.now().year}"""")
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
                events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
            }
        }

        managedDevices {
            localDevices {
                create("pixel2api27") {
                    device = "Pixel 2"
                    apiLevel = 27
                    systemImageSource = "aosp"
                }
                create("pixel2targetApi") {
                    device = "Pixel 2"
                    apiLevel =
                        libs.versions.targetSdk
                            .get()
                            .toInt()
                    // TODO "aosp-atd" not available for api 35
                    systemImageSource = "google"
                }

                create("pixelTabletTargetApi") {
                    device = "Pixel Tablet"
                    apiLevel =
                        libs.versions.targetSdk
                            .get()
                            .toInt()
                    // TODO "aosp-atd" not available for api 35
                    systemImageSource = "google"
                }
            }

            groups {
                create("allDevices") {
                    targetDevices.addAll(devices)
                }
            }
        }
    }

    signingConfigs {
        named("debug") {
            storeFile = rootDir.resolve("keystore/debug.keystore")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs["debug"]
        }
    }
}

licenses {
    reports {
        // reset the styling; we use a css in assets
        val css = ""
        html.stylesheet(css)
    }
}

kotlin {
    jvmToolchain(17)
}

tasks {
    check {
        dependsOn(verifyPaparazzi)
    }
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
    implementation(libs.androidx.webkit)
    implementation(libs.haze.haze)
    implementation(libs.haze.materials)
    implementation(libs.timber)

    implementation(libs.cmgapps.logtag.logtag)
    ksp(libs.cmgapps.logtag.processor)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.vintage.engine)
    testImplementation(libs.hamcrest)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}
