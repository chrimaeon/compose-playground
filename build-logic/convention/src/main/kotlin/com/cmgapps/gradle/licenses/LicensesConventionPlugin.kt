/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.gradle.licenses

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import com.cmgapps.license.LicensesPlugin
import com.cmgapps.license.LicensesTask
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

@Suppress("unused")
class LicensesConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<AppPlugin>()
            apply<LicensesPlugin>()

            extensions.configure<ApplicationAndroidComponentsExtension> {
                onVariants { variant ->
                    val licenseTask =
                        project.tasks.named<LicensesTask>("license${variant.name.uppercaseFirstChar()}Report")

                    val copyLicense =
                        project.tasks.register<CopyLicenseTask>(
                            "copyLicense${variant.name.uppercaseFirstChar()}Report",
                        ) {
                            outputs.upToDateWhen { false }

                            licenseFile.set(
                                licenseTask.map {
                                    it.reports.html.outputLocation
                                        .get()
                                },
                            )
                        }

                    variant.sources.assets?.addGeneratedSourceDirectory(
                        copyLicense,
                        CopyLicenseTask::outputDirectory,
                    )
                }
            }
        }
    }
}

abstract class CopyLicenseTask : DefaultTask() {
    @get:InputFile
    abstract val licenseFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun taskAction() {
        val outputDirFile = outputDirectory.get().asFile
        val licenseFile = licenseFile.get().asFile

        logger.info("Copying license ${licenseFile.absoluteFile} to ${outputDirFile.absolutePath}")
        licenseFile.copyTo(outputDirFile.resolve(licenseFile.name), overwrite = true)
    }
}
