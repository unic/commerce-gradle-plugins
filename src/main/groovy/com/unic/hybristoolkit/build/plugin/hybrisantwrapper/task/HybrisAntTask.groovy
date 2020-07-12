/*
 * Copyright (c) 2019 Unic AG
 */
package com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task

import com.google.common.io.PatternFilenameFilter
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
     * If defined, the value of this property will be passed to ant as 'testclasses.packages.excluded' system property.
     */
    final Property<String> testpackagesExcluded = project.objects.property(String)

    /**
     * Contains the path to the hybris installation (absolute or relative to projectDir).
     */
    final Property<File> hybrisExtractionDir = project.objects.property(File)

    /**
     * The arguments to pass to the ant runner. Usually you would at least specify a target to execute.
     */
    def arguments = []

    /**
     * System properties to set. Each property will be passed in the form of '-Dkey=value'.
     */
    def taskProperties = new Properties()

    @TaskAction
    def runHybrisTask() {
        File platformHome = new File(hybrisExtractionDir.get(), 'hybris/bin/platform')
        // TODO detect if no apache ant was found, thus expect hybris not to be in place.
        // TODO add dependency on configureHybris for all ant tasks (what about InitialHybrisBuild?)
        File antHome = platformHome.listFiles(new PatternFilenameFilter("apache-ant.*")).first()

        project.javaexec {

            classpath(new File(antHome, 'lib/ant-launcher.jar'))
            workingDir(platformHome)
            main = 'org.apache.tools.ant.launch.Launcher'
            systemProperties taskProperties
            systemProperty 'ant.home', antHome
            systemProperty 'input.template', 'develop'
            systemProperty 'maven.update.dbdrivers', 'false'

            if (testpackages.isPresent()) {
                systemProperty 'testclasses.packages', "${testpackages.get()}"
            }
            if (testpackagesExcluded.isPresent()) {
                systemProperty 'testclasses.packages.excluded', "${testpackagesExcluded.get()}"

                // Hybris sets the 'testclasses.packages.excluded' property by default. This overrides any values
                // provided as system property, even though if it should be configurable that way.
                // Remove the property from the project.properties as a workaround.
                File configfile = new File(platformHome, 'project.properties')
                configfile.text = configfile.text - ~/(?mi)\s+^testclasses.packages.excluded\s*=.*$/
            }
            args arguments
        }
    }

    def systemProperty(key, value) {
        taskProperties.setProperty(key, value)
    }
}
