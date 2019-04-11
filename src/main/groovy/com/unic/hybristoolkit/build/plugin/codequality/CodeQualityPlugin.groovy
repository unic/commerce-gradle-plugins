/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.codequality

import com.unic.hybristoolkit.build.plugin.codequality.task.SonarRunner
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Wraps the {@link com.unic.hybristoolkit.build.plugin.hybrisantwrapper.HybrisAntWrapperPlugin} adding the jacoco agent configuration to the local.properties before each test run and removing it afterwards.
 */
class CodeQualityPlugin implements Plugin<Project> {

    def extension
    def hybrisAntWrapperExtension
    def jacocoConfiguration
    def sonarrunnerConfiguration
    def projectDir

    @Override
    void apply(Project project) {

        extension = project.extensions.create("codeQuality", CodeQualityExtension, project)
        hybrisAntWrapperExtension = project.extensions.hybrisAntWrapper
        projectDir = project.projectDir

        project.pluginManager.apply('com.unic.hybristoolkit.build.plugin.hybrisantwrapper')

        project.tasks.hybrisUnittests {
            doFirst { enableJacoco('unittests') }
            doLast { disableJacoco() }
        }

        project.tasks.hybrisIntegrationtests {
            doFirst { enableJacoco('integrationtests') }
            doLast { disableJacoco() }
        }

        project.tasks.hybrisWebUnittests {
            doFirst { enableJacoco('webunittests') }
            doLast { disableJacoco() }
        }

        project.tasks.hybrisWebIntegrationtests {
            doFirst { enableJacoco('webintegrationtests') }
            doLast { disableJacoco() }
        }

        project.tasks.hybrisAlltests {
            doFirst { enableJacoco('alltests') }
            doLast { disableJacoco() }
        }

        project.tasks.create('sonar', SonarRunner) {
            sonarClasspath = extension.sonarClasspath
            sonarWorkingDir = project.projectDir
            sonarProjectDir = project.projectDir
        }

        project.afterEvaluate {
            project.configurations {
                codeQualityJacoco
                codeQualitySonarrunner
            }
            project.dependencies {
                codeQualityJacoco project.extensions.codeQuality.jacocoDependency
                codeQualitySonarrunner project.extensions.codeQuality.sonarrunnerDependency
            }
            jacocoConfiguration = project.configurations.codeQualityJacoco
            sonarrunnerConfiguration = project.configurations.codeQualitySonarrunner

            // pass the sonar jar to the SonarRunner (to be used as classpath)
            extension.sonarClasspath.set(project.files(sonarrunnerConfiguration.singleFile))
        }
    }


    def enableJacoco(classifier) {
        println("BEWARE: Appending Jacoco configuration for ${classifier} tests to 'standalone.javaoptions'!")
        def destdir = new File("${hybrisAntWrapperExtension.hybrisExtractionDir.get()}/hybris/log/jacoco/")
        def destfile = destdir.toPath().resolve("jacoco-${classifier}.exec").toFile()
        println("Jacoco report output file: ${destfile}")
        def args = "-DjacocoConf=start -javaagent:${jacocoConfiguration.singleFile}=destfile=${destfile},append=true,excludes=*Test -DjacocoConf=end"

        getConfigFile().text = getConfigFile().text.replaceAll(/(?m)^(standalone\.javaoptions=.*?)(-DjacocoConf=start.*-DjacocoConf=end)?$/, '$1 ' + args)
    }

    def disableJacoco() {
        println("BEWARE: Removing Jacoco configuration from 'standalone.javaoptions'")

        getConfigFile().text = getConfigFile().text.replaceAll(/(?m)^(standalone\.javaoptions=.*?)(-DjacocoConf=start.*-DjacocoConf=end)?$/, '$1')
    }

    def getConfigFile(){
        new File(hybrisAntWrapperExtension.hybrisExtractionDir.get(),'hybris/config/local.properties')
    }
}