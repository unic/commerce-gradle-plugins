/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.codequality

import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property

/**
 * Extension class for the HybrisAntWrapper extension
 */
class CodeQualityExtension {

    def project
    final Property<FileCollection> sonarClasspath

    /**
     * The depencency string used to fetch the jacoco agent jar.
     */
    def jacocoDependency = 'org.jacoco:org.jacoco.agent:0.7.7.201606060606:runtime'
    /**
     * The depencency string used to fetch the sonarrunner jar.
     */
    def sonarrunnerDependency = 'org.sonarsource.scanner.cli:sonar-scanner-cli:3.1.0.1141@jar'


    CodeQualityExtension(project) {
        this.project = project
        sonarClasspath = project.objects.property(FileCollection)
    }

    void setJacocoDependency(jacocoDependency) {
        this.jacocoDependency = jacocoDependency
    }

    void setSonarDependency(sonarDependency) {
        this.sonarDependency = sonarDependency
    }
}
