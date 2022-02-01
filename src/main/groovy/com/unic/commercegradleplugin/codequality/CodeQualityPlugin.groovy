/*
 * Copyright (c) 2022 Unic AG
 */

package com.unic.commercegradleplugin.codequality

import com.unic.commercegradleplugin.codequality.rule.JacocoInstrumentationRule
import com.unic.commercegradleplugin.codequality.task.SonarRunner
import com.unic.commercegradleplugin.codequality.task.JacocoReport
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Provides tasks to enable Jacoco instrumentation and Sonar analysis for SAP Commerce projects.
 */
class CodeQualityPlugin implements Plugin<Project> {

    def extension
    def jacocoConfiguration
    def jacocoCliConfiguration
    def sonarRunnerConfiguration
    File hybrisDir
    JacocoInstrumentationRule jacocoInstrumentationRule

    @Override
    void apply(Project project) {

        extension = project.extensions.create("codeQuality", CodeQualityExtension, project)
        hybrisDir = new File("${project.projectDir}/hybris")

        jacocoInstrumentationRule = new JacocoInstrumentationRule(project, this.hybrisDir)
        project.tasks.addRule(jacocoInstrumentationRule)

        project.tasks.create('sonar', SonarRunner) {
            sonarClasspath = extension.sonarClasspath
            sonarWorkingDir = project.projectDir
            sonarProjectDir = project.projectDir
        }

        project.tasks.create('jacocoReport', JacocoReport) {
            jacocoCliClasspath = extension.jacocoCliClasspath
            customCodePath = extension.customCodePath
            jacocoCliWorkingDir = project.projectDir
            hybrisDir = this.hybrisDir
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
            sonarRunnerConfiguration = project.configurations.codeQualitySonarrunner

            // pass the sonar jar to the SonarRunner task (to be used as classpath)
            extension.sonarClasspath.set(project.files(sonarRunnerConfiguration.singleFile))
            // pass the jacoco-cli jar to the JacocoReport task (to be used as classpath)
            extension.jacocoCliClasspath.set(project.files(jacocoCliConfiguration.singleFile))
            // pass the jacoco-agent jar to the JacocoInstrumentationRule
            jacocoInstrumentationRule.jacocoJar.set(jacocoConfiguration.singleFile)
        }
    }
}