/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.codequality


import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Wraps the {@link com.unic.hybristoolkit.build.plugin.hybrisantwrapper.HybrisAntWrapperPlugin} adding the jacoco agent configuration to the local.properties before each test run and removing it afterwards.
 */
class CodeQualityPlugin implements Plugin<Project> {

    def myExtension
    def jacocoConfiguration
    def projectDir

    @Override
    void apply(Project project) {

        myExtension = project.extensions.create("codeQuality", CodeQualityExtension, project)
        projectDir = project.projectDir

        project.pluginManager.apply('com.unic.hybristoolkit.build.plugin.hybrisantwrapper')

        project.tasks.hybrisUnittests {
            doFirst { enableJacoco('unittests') }
            doLast { disableJacoco('unittests') }
        }

        project.tasks.hybrisIntegrationtests {
            doFirst { enableJacoco('integrationtests') }
            doLast { disableJacoco('integrationtests') }
        }

        project.tasks.hybrisWebUnittests {
            doFirst { enableJacoco('webunittests') }
            doLast { disableJacoco('webunittests') }
        }

        project.tasks.hybrisWebIntegrationtests {
            doFirst { enableJacoco('webintegrationtests') }
            doLast { disableJacoco('webintegrationtests') }
        }

        project.tasks.hybrisAlltests {
            doFirst { enableJacoco('alltests') }
            doLast { disableJacoco('alltests') }
        }

        project.afterEvaluate {
            jacocoConfiguration = project.configurations.create("codeQualityJacoco")
            project.configurations.codeQualityJacoco.dependencies.add(
                    project.dependencies.add("codeQualityJacoco", project.extensions.codeQuality.jacocoDependency)
            )
        }


    }


    def enableJacoco(classifier) {
        println("BEWARE: Appending Jacoco configuration for integration tests to 'standalone.javaoptions'!")
        def args = "-DjacocoConf=start -javaagent:${jacocoConfiguration.singleFile}=destfile=${projectDir}/hybris/log/jacoco/jacoco-${classifier}.exec,append=true,excludes=*Test -DjacocoConf=end"
        myExtension.configFile.text = myExtension.configFile.text.replaceAll(/(?m)^(standalone\.javaoptions=.*?)(-DjacocoConf=start.*-DjacocoConf=end)?$/, '$1 ' + args)
    }

    def disableJacoco() {
        println("BEWARE: Removing Jacoco configuration from 'standalone.javaoptions'")
        myExtension.configFile.text = myExtension.configFile.text.replaceAll(/(?m)^(standalone\.javaoptions=.*?)(-DjacocoConf=start.*-DjacocoConf=end)?$/, '$1')
    }
}