/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.installmysqldriver

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy

/**
 * A plugin that downloads and installs a mysql-connector-java jar, which must be defined as dependency for the 'mysql' configuration.
 */
class InstallMysqlDriverPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def extension = project.extensions.create('installMysql', InstallMysqlDriverExtension)

        def hybrisAntWrapperExt = project.extensions.hybrisAntWrapper

        def jarName = "mysql-connector-java.jar"

        def installMysqlDriverTask = project.task("installMysqlDriver", type: Copy) {
            description "Installs MySQL connection library into hybris."
            group "Unic Hybris Toolkit"

            outputs.upToDateWhen {
                new File(hybrisAntWrapperExt.hybrisExtractionDir.get(), "hybris/bin/platform/lib/dbdriver/${jarName}").exists()
            }

            doFirst {
                def hybrisDbDriverDir = new File(hybrisAntWrapperExt.hybrisExtractionDir.get(), 'hybris/bin/platform/lib/dbdriver')
                println ">>> Copy mysql connection lib to $hybrisDbDriverDir ..."
            }

            from {
                project.configurations.mysql.collect { it }
            }
            into { new File(hybrisAntWrapperExt.hybrisExtractionDir.get(), 'hybris/bin/platform/lib/dbdriver') }
            rename(".*", jarName)

            doLast {
                def hybrisDbDriverDir = new File(hybrisAntWrapperExt.hybrisExtractionDir.get(), 'hybris/bin/platform/lib/dbdriver')
                println ">>> Touch lastupdate file in ${hybrisDbDriverDir} ..."
                new File("${hybrisDbDriverDir}/.lastupdate").setLastModified(System.currentTimeMillis())
            }
        }

        installMysqlDriverTask.dependsOn('extractHybris')

        project.afterEvaluate {
            project.configurations {
                mysql
            }
            project.dependencies {
                mysql extension.mysqlDependency
            }
        }
    }
}
