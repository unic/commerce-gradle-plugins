/*
 * Copyright (c) 2022 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.codequality

import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property

/**
 * Extension class for the CodeQualityExtension extension
 */
class CodeQualityExtension {

    final Property<FileCollection> sonarClasspath
    final Property<FileCollection> jacocoCliClasspath

    /**
     * The depencency string used to fetch the jacoco agent jar.
     */
    def jacocoDependency = 'org.jacoco:org.jacoco.agent:0.8.7:runtime'

    /**
     * The depencency string used to fetch the jacoco cli jar.
     */
    def jacocoCliDependency = 'org.jacoco:org.jacoco.cli:0.8.7:nodeps@jar'
    /**
     * The depencency string used to fetch the sonarrunner jar.
     */
    def sonarrunnerDependency = 'org.sonarsource.scanner.cli:sonar-scanner-cli:4.6.2.2472@jar'
    /**
     * The targets that should be 'enhanced' with the Jacoco agent.
     */
    def jacocoTargets = ['cloudTests', 'cloudWebTests']


    CodeQualityExtension(project) {
        this.sonarClasspath = project.objects.property(FileCollection)
        this.jacocoCliClasspath = project.objects.property(FileCollection)
    }

    void setJacocoDependency(jacocoDependency) {
        this.jacocoDependency = jacocoDependency
    }

    void setJacocoCliDependency(jacocoCliDependency) {
        this.jacocoCliDependency = jacocoCliDependency
    }

    void setSonarDependency(sonarDependency) {
        this.sonarDependency = sonarDependency
    }

    void setJacocoTargets(targets) {
        this.jacocoTargets = targets
    }
}
