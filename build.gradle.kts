import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel

/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.licenses) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.versions)
}

tasks {
    wrapper {
        gradleVersion = libs.versions.gradle.get()
        distributionType = Wrapper.DistributionType.ALL
    }

    withType<DependencyUpdatesTask> {
        revision = "release"

        rejectVersionIf {
            listOf("alpha", "beta", "rc", "cr", "m", "eap", "dev").any { qualifier ->
                """(?i).*[.-]?$qualifier[.\d-]*"""
                    .toRegex()
                    .containsMatchIn(candidate.version)
            }
        }

        gradleReleaseChannel = GradleReleaseChannel.CURRENT.id
    }

    register<Delete>("clean") {
        delete(rootProject.layout.buildDirectory)
    }
}
