/*
 * Copyright (c) 2019 Unic AG
 */
package com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction

/**
 * Provides an environment to execute hybris ant tasks with the ant installation provided by hybris.
 */
class HybrisAntTask extends DefaultTask {
    /**
     * If defined, the value of this property will be passed to ant as 'testclasses.packages' system property.
     */
    final Property<String> testpackages = project.objects.property(String)
    /**
    * The arguments to pass to the ant runner. Usually you would at least specify a target to execute.
    */
    def arguments
    /**
     * System properties to set. Each property will be passed in the form of '-Dkey=value'.
     */
    def taskProperties = new Properties()

    @TaskAction
    def runHybrisTask() {
        project.javaexec {
            classpath(project.file('hybris/bin/platform/apache-ant-1.9.1/lib/ant-launcher.jar'))
            workingDir(project.file('hybris/bin/platform'))
            main = 'org.apache.tools.ant.launch.Launcher'
            systemProperties taskProperties
            systemProperty 'ant.home', project.file('hybris/bin/platform/apache-ant-1.9.1')
            systemProperty 'input.template', 'develop'
            systemProperty 'maven.update.dbdrivers', 'false'

            if(testpackages.isPresent()) {
                systemProperty 'testclasses.packages', testpackages.get()
            }
            args arguments
        }
    }

    def systemProperty(key, value) {
        taskProperties.setProperty(key, value)
    }
}