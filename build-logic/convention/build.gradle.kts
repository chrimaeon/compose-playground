/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

plugins {
    `kotlin-dsl`
}

group = "com.cmgapps.gradle.convention"

gradlePlugin {
    plugins {
        register("licensePlugin") {
            id = "licenses"
            implementationClass = "com.cmgapps.gradle.licenses.LicensesConventionPlugin"
        }
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.licenses.gradle.plugin)
}
