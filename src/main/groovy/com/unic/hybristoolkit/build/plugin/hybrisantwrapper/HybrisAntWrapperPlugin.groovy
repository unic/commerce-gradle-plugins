/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.hybrisantwrapper

import com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task.ConfigureHybrisTask
import com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task.ExtractHybrisTask
import com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task.HybrisAntTask
import com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task.InitialHybrisBuildTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A Plugin that aims to simplify building and packaging hybris by wrapping its ant targets to be easily used in gradle
 * builds.
 * <p>
 * Additionally it provides all all steps required to download hybris, build it initially and configure it, to get it
 * ready for a build in the first place.
 * </p>
 */
class HybrisAntWrapperPlugin implements Plugin<Project> {
    def extension

    @Override
    void apply(Project project) {
        extension = project.extensions.create("hybrisAntWrapper", HybrisAntWrapperExtension, project)

        project.tasks.withType(HybrisAntTask) {
            hybrisExtractionDir = extension.hybrisExtractionDir
        }

        project.tasks.create('hybrisAll', HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant all' target."
            arguments = 'all'
        }

        project.tasks.create('hybrisClean', HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant clean' target."
            arguments = 'clean'
        }

        project.tasks.create('hybrisUnittests', HybrisAntTask) {
            doFirst { adjustJunitReportAndTempDirectory(project) }
            doLast { resetJunitReportAndTempDirectory(project) }
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant unittests' target."
            systemProperty 'testclasses.annotations', 'unittests'
            systemProperty 'testclasses.junit.results.directory', '${HYBRIS_LOG_DIR}/junit/unittests'
            systemProperty 'testclasses.junit.temp.directory', '${HYBRIS_TEMP_DIR}/junit/unittests'
            systemProperty 'testclasses.suppress.junit.tenant', 'true'
            testpackages = extension.testpackages
            arguments = 'unittests'
        }

        project.tasks.create('hybrisWebUnittests', HybrisAntTask) {
            doFirst { adjustJunitReportAndTempDirectory(project) }
            doLast { resetJunitReportAndTempDirectory(project) }
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant allwebtests' target with an annotation-limitation for unittests."
            systemProperty 'testclasses.annotations', 'unittests'
            systemProperty 'testclasses.suppress.junit.tenant', 'true'
            systemProperty 'testclasses.junit.results.directory', '${HYBRIS_LOG_DIR}/junit/webunittests'
            systemProperty 'testclasses.junit.temp.directory', '${HYBRIS_TEMP_DIR}/junit/webunittests'
            testpackages = extension.testpackages
            arguments = 'allwebtests'
        }

        project.tasks.create('hybrisIntegrationtests', HybrisAntTask) {
            doFirst { adjustJunitReportAndTempDirectory(project) }
            doLast { resetJunitReportAndTempDirectory(project) }
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant integrationtests' target."
            systemProperty 'testclasses.junit.results.directory', '${HYBRIS_LOG_DIR}/junit/integrationtests'
            systemProperty 'testclasses.junit.temp.directory', '${HYBRIS_TEMP_DIR}/junit/integrationtests'
            testpackages = extension.testpackages
            arguments = 'integrationtests'
        }

        project.tasks.create('hybrisWebIntegrationtests', HybrisAntTask) {
            doFirst { adjustJunitReportAndTempDirectory(project) }
            doLast { resetJunitReportAndTempDirectory(project) }
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant allwebtests' target with an annotation-limitation for integrationtests."
            systemProperty 'testclasses.annotations', 'integrationtests'
            systemProperty 'testclasses.junit.results.directory', '${HYBRIS_LOG_DIR}/junit/webintegrationtests'
            systemProperty 'testclasses.junit.temp.directory', '${HYBRIS_TEMP_DIR}/junit/webintegrationtests'
            testpackages = extension.testpackages
            arguments = 'allwebtests'
        }

        project.tasks.create('hybrisAlltests', HybrisAntTask) {
            doFirst { adjustJunitReportAndTempDirectory(project) }
            doLast { resetJunitReportAndTempDirectory(project) }
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant alltests' target."
            systemProperty 'testclasses.junit.results.directory', '${HYBRIS_LOG_DIR}/junit/alltests'
            systemProperty 'testclasses.junit.temp.directory', '${HYBRIS_TEMP_DIR}/junit/alltests'
            testpackages = extension.testpackages
            arguments = 'alltests'
        }

        project.tasks.create('hybrisInitialize', HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Initializes hybris."
            arguments = 'initialize'
        }

        project.tasks.create('hybrisYunitinit', HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Initializes JUnit tenant for hybris."
            arguments = 'yunitinit'
        }

        project.tasks.create('hybrisProduction', HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant production' target."
            arguments = ['production', '-Dde.hybris.platform.ant.production.skip.build=true', '-Dproduction.legacy.mode=false']
        }

        def extractHybrisTask = project.tasks.create('extractHybris', ExtractHybrisTask) {
            group "Unic Hybris Toolkit"
            description = 'Extracts hybris to the specified target'
        }

        def initialHybrisBuildTask = project.tasks.create('initialHybrisBuild', InitialHybrisBuildTask) {
            group "Unic Hybris Toolkit"
            description = 'If no config folder exists yet this task runs an initial build to create all necessary folders.'
        }

        def configureHybrisTask = project.tasks.create('configureHybris', ConfigureHybrisTask) {
            group "Unic Hybris Toolkit"
            description "Merges local configuration and customizations with hybris installation."
            hybrisExtractionDir = extension.hybrisExtractionDir
        }

        initialHybrisBuildTask.dependsOn(extractHybrisTask)
        configureHybrisTask.dependsOn(initialHybrisBuildTask)

        project.afterEvaluate {
            project.configurations {
                hybris
            }
            project.dependencies {
                hybris extension.hybrisDependency
            }
            project.tasks.extractHybris.hybrisZip.set(project.configurations.hybris.singleFile)
        }
    }


    /**
     * Modifies the ant test targets to output junit reports to hybris/log/junit
     * @param project the project to modify
     */
    def adjustJunitReportAndTempDirectory(project) {
        project.ant.replaceregexp(match: '\\$\\{HYBRIS_LOG_DIR\\}\\/junit',
                replace: '${testclasses.junit.results.directory}',
                flags: 'g',
                byline: true) {
            fileset(dir: "${ extension.hybrisExtractionDir.get()}/hybris/bin/platform/resources/ant", includes: 'testing.xml')
        }

        project.ant.replaceregexp(match: '\\$\\{HYBRIS_TEMP_DIR\\}\\/junit',
                replace: '${testclasses.junit.temp.directory}',
                flags: 'g',
                byline: true) {
            fileset(dir: "${ extension.hybrisExtractionDir.get()}/hybris/bin/platform/resources/ant", includes: 'testing.xml')
        }
    }

    /**
     * resets the default output behaviour of ant test targets
     * @param project the project to modify
     */
    def resetJunitReportAndTempDirectory(project) {
        project.ant.replaceregexp(match: '\\$\\{testclasses.junit.results.directory\\}',
                replace: '${HYBRIS_LOG_DIR}/junit',
                flags: 'g',
                byline: true) {
            fileset(dir: "${ extension.hybrisExtractionDir.get()}/hybris/bin/platform/resources/ant", includes: 'testing.xml')
        }

        project.ant.replaceregexp(match: '\\$\\{testclasses.junit.temp.directory\\}',
                replace: '${HYBRIS_TEMP_DIR}/junit',
                flags: 'g',
                byline: true) {
            fileset(dir: "${ extension.hybrisExtractionDir.get()}/hybris/bin/platform/resources/ant", includes: 'testing.xml')
        }
    }
}