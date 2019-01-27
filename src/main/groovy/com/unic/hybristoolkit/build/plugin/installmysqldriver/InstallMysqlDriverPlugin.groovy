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
        //project.configurations.create("mysql")
        //project.configurations.mysql.add("mysql:mysql-connector-java:${mysqlConnectorVersion}")

        def jarName = "mysql-connector-java.jar"
        def mysqlConnectionLib = new File(project.projectDir, "hybris/bin/platform/lib/dbdriver/${jarName}")
        def hybrisDbDriverDir = new File(project.projectDir, 'hybris/bin/platform/lib/dbdriver')

        def installMysqlDriverTask = project.task("installMysqlDriver", type: Copy) {

            description "Installs MySQL connection library into hybris."
            group "Unic Hybris Toolkit"

            outputs.upToDateWhen { mysqlConnectionLib.exists() }

            doFirst {
                println ">>> Copy mysql connection lib to $mysqlConnectionLib ..."
            }

            from {
                project.configurations.mysql.collect { it }
            }
            into hybrisDbDriverDir
            rename(".*", jarName)

            doLast {
                println ">>> Touch lastupdate file in hybris/bin/platform/lib/dbdriver ..."
                new File("$project.projectDir/hybris/bin/platform/lib/dbdriver/.lastupdate").setLastModified(System.currentTimeMillis())
            }
        }

        installMysqlDriverTask.dependsOn('extractHybris')
    }
}
