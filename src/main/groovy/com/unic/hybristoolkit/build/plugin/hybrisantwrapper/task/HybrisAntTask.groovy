/*
 * Copyright (c) 2019 Unic AG
 */
package com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task


import org.gradle.api.tasks.JavaExec

class HybrisAntTask extends JavaExec {

    HybrisAntTask() {
        classpath(project.file('hybris/bin/platform/apache-ant-1.9.1/lib/ant-launcher.jar'))
        workingDir(project.file('hybris/bin/platform'))
        main = 'org.apache.tools.ant.launch.Launcher'
        systemProperty 'ant.home', project.file('hybris/bin/platform/apache-ant-1.9.1')
        systemProperty 'input.template', 'develop'
        systemProperty 'maven.update.dbdrivers', 'false'

    }

}