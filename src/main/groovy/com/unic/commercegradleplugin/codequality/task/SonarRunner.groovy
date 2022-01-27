/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.commercegradleplugin.codequality.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction

/**
 * Runs the Sonar Scanner on the project directory.
 * See: https://docs.sonarqube.org/latest/analysis/scan/sonarscanner/
 *
 * Use this in cases where you can's use the official sonar runner:
 * https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Gradle
 */
class SonarRunner extends DefaultTask {

    final Property<FileCollection> sonarClasspath = project.objects.property(FileCollection)
    def sonarWorkingDir
    def sonarProjectDir
    def sonarProperties = new Properties()

    @TaskAction
    void sonarrunner() {
        println properties
        project.javaexec {
            classpath = sonarClasspath.get()
            main = 'org.sonarsource.scanner.cli.Main'
            workingDir = sonarWorkingDir
            systemProperties sonarProperties
            systemProperty 'java.awt.headles', 'true'
            systemProperty 'project.home', sonarProjectDir

        }
    }

    def sonarProperty(key, value) {
        sonarProperties.setProperty(key, value)
    }

}
