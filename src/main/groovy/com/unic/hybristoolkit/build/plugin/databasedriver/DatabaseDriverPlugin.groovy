/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.databasedriver

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

/**
 * A plugin that downloads and installs a JDBC jar, which must be defined as dependency for the 'databaseDriver' configuration.
 */
class DatabaseDriverPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def extension = project.extensions.create('databaseDriver', DatabaseDriverExtension, project)

        def hybrisAntWrapperExt = project.extensions.hybrisAntWrapper

        def installDbDriver = project.task("installDatabaseDriver", type: Copy) {
            description "Installs defined database connection library into hybris."
            group "Unic Hybris Toolkit"

            outputs.upToDateWhen {
                new File(hybrisAntWrapperExt.hybrisExtractionDir.get(), "hybris/bin/platform/lib/dbdriver/${extension.jarName.get()}").exists()
            }

            doFirst {
                def hybrisDbDriverDir = new File(hybrisAntWrapperExt.hybrisExtractionDir.get(), 'hybris/bin/platform/lib/dbdriver')
                println ">>> Copy jdbc connection lib to $hybrisDbDriverDir ..."
            }

            from {
                project.configurations.dbDriver.collect { it }
            }
            into { new File(hybrisAntWrapperExt.hybrisExtractionDir.get(), 'hybris/bin/platform/lib/dbdriver') }
            rename {
                filename -> extension.jarName.get()
            }

            doLast {
                def hybrisDbDriverDir = new File(hybrisAntWrapperExt.hybrisExtractionDir.get(), 'hybris/bin/platform/lib/dbdriver')
                println ">>> Touch lastupdate file in ${hybrisDbDriverDir} ..."
                new File("${hybrisDbDriverDir}/.lastupdate").setLastModified(System.currentTimeMillis())
            }
        }

        installDbDriver.dependsOn('extractHybris')

        project.afterEvaluate {
            project.configurations {
                dbDriver
            }
            project.dependencies {
                dbDriver extension.jdbcDependency
            }
        }
    }
}
