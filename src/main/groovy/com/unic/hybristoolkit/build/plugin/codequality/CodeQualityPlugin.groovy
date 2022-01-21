/*
 * Copyright (c) 2022 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.codequality

import com.unic.hybristoolkit.build.plugin.codequality.task.SonarRunner
import com.unic.hybristoolkit.build.plugin.codequality.task.JacocoReport
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Allows for adding the jacoco agent configuration to the local.properties before each test run and removing it  automatically afterwards.
 */
class CodeQualityPlugin implements Plugin<Project> {

    def extension
    def jacocoConfiguration
    def jacocoCliConfiguration
    def sonarrunnerConfiguration
    def projectDir
    def hybrisDir

    @Override
    void apply(Project project) {

        extension = project.extensions.create("codeQuality", CodeQualityExtension, project)
        projectDir = project.projectDir

        extension.jacocoTargets.each { target ->
            project.tasks.named(target) {
                doFirst { enableJacoco(target) }
                doLast { disableJacoco() }
            }
        }

        project.tasks.create('sonar', SonarRunner) {
            sonarClasspath = extension.sonarClasspath
            sonarWorkingDir = project.projectDir
            sonarProjectDir = project.projectDir
        }

        project.tasks.create('jacocoReport', JacocoReport) {
            jacocoCliClasspath = extension.jacocoCliClasspath
            jacocoCliWorkingDir = project.projectDir
            this.hybrisDir = new File("${project.projectDir}/hybris")
        }

        project.afterEvaluate {
            project.configurations {
                codeQualityJacoco
                codeQualityJacocoCli
                codeQualitySonarrunner
            }
            project.dependencies {
                codeQualityJacoco project.extensions.codeQuality.jacocoDependency
                codeQualityJacocoCli project.extensions.codeQuality.jacocoCliDependency
                codeQualitySonarrunner project.extensions.codeQuality.sonarrunnerDependency
            }
            jacocoConfiguration = project.configurations.codeQualityJacoco
            jacocoCliConfiguration = project.configurations.codeQualityJacocoCli
            sonarrunnerConfiguration = project.configurations.codeQualitySonarrunner

            // pass the sonar jar to the SonarRunner task (to be used as classpath)
            extension.sonarClasspath.set(project.files(sonarrunnerConfiguration.singleFile))
            // pass the jacoco-cli jar to the JacocoReport task (to be used as classpath)
            extension.jacocoCliClasspath.set(project.files(jacocoCliConfiguration.singleFile))
        }
    }


    def enableJacoco(classifier) {
        println("BEWARE: Appending Jacoco configuration for ${classifier} tests to 'standalone.javaoptions'!")
        def destdir = new File(hybrisDir, "log/jacoco/")
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
        new File(hybrisDir, 'config/local.properties')
    }
}