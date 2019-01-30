/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.codequality

/**
 * Extension class for the HybrisAntWrapper extension
 */
class CodeQualityExtension {
    def jacocoDependency = 'org.jacoco:org.jacoco.agent:0.7.7.201606060606:runtime'
    def configFile

    CodeQualityExtension(project) {
        configFile = project.file('hybris/config/local.properties')
    }

    void setJacocoDependency(jacocoDependency) {
        this.jacocoDependency = jacocoDependency
    }

    void setConfigFile(configFile) {
        this.configFile = configFile instanceof String ? project.file(configPath) : configFile
    }
}
