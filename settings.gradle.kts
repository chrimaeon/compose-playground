/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

@file:Suppress("UnstableApiUsage")

import de.fayard.refreshVersions.core.FeatureFlag
import de.fayard.refreshVersions.core.StabilityLevel

rootProject.name = "Compose Playground"

pluginManagement {
    includeBuild("./build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.60.6"
}

refreshVersions {
    featureFlags {
        enable(FeatureFlag.GRADLE_UPDATES)
    }

    rejectVersionIf {
        candidate.stabilityLevel != StabilityLevel.Stable
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "AndroidX Snapshot"
            // Used for androidx-material3-adaptive-navigation3
            url = uri("https://androidx.dev/snapshots/builds/14037610/artifacts/repository")
        }
    }
}

include(":app")
