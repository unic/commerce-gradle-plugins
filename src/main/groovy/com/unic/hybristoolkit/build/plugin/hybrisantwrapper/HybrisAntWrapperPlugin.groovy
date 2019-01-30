/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.hybrisantwrapper

import com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task.HybrisAntTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class HybrisAntWrapperPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.task('hybrisAll', type: HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant all' target."
            args 'all'
        }

        project.task('hybrisClean', type: HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant clean' target."
            args 'clean'
        }

        project.task('hybrisUnittests', type: HybrisAntTask) {
            doFirst { adjustJunitReportAndTempDirectory(project) }
            doLast { resetJunitReportAndTempDirectory(project) }
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant unittests' target."
            systemProperty 'testclasses.annotations', 'unittests'
            systemProperty 'testclasses.junit.results.directory', '${HYBRIS_LOG_DIR}/junit/unittests'
            systemProperty 'testclasses.junit.temp.directory', '${HYBRIS_TEMP_DIR}/junit/unittests'
            systemProperty 'testclasses.suppress.junit.tenant', 'true'
            args 'unittests'
        }

        project.task('hybrisWebUnittests', type: HybrisAntTask) {
            doFirst { adjustJunitReportAndTempDirectory(project) }
            doLast { resetJunitReportAndTempDirectory(project) }
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant allwebtests' target with an annotation-limitation for unittests."
            systemProperty 'testclasses.annotations', 'unittests'
            systemProperty 'testclasses.suppress.junit.tenant', 'true'
            systemProperty 'testclasses.junit.results.directory', '${HYBRIS_LOG_DIR}/junit/webunittests'
            systemProperty 'testclasses.junit.temp.directory', '${HYBRIS_TEMP_DIR}/junit/webunittests'
            args 'allwebtests'
        }

        project.task('hybrisIntegrationtests', type: HybrisAntTask) {
            doFirst { adjustJunitReportAndTempDirectory(project) }
            doLast { resetJunitReportAndTempDirectory(project) }
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant integrationtests' target."
            systemProperty 'testclasses.junit.results.directory', '${HYBRIS_LOG_DIR}/junit/integrationtests'
            systemProperty 'testclasses.junit.temp.directory', '${HYBRIS_TEMP_DIR}/junit/integrationtests'
            args 'integrationtests'
        }

        project.task('hybrisWebIntegrationtests', type: HybrisAntTask) {
            doFirst { adjustJunitReportAndTempDirectory(project) }
            doLast { resetJunitReportAndTempDirectory(project) }
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant allwebtests' target with an annotation-limitation for integrationtests."
            systemProperty 'testclasses.annotations', 'integrationtests'
            systemProperty 'testclasses.junit.results.directory', '${HYBRIS_LOG_DIR}/junit/webintegrationtests'
            systemProperty 'testclasses.junit.temp.directory', '${HYBRIS_TEMP_DIR}/junit/webintegrationtests'
            args 'allwebtests'
        }

        project.task('hybrisAlltests', type: HybrisAntTask) {
            doFirst { adjustJunitReportAndTempDirectory(project) }
            doLast { resetJunitReportAndTempDirectory(project) }
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant alltests' target."
            systemProperty 'testclasses.junit.results.directory', '${HYBRIS_LOG_DIR}/junit/alltests'
            systemProperty 'testclasses.junit.temp.directory', '${HYBRIS_TEMP_DIR}/junit/alltests'
            args 'alltests'
        }

        project.task('hybrisInitialize', type: HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Initializes hybris."
            args 'initialize'
        }

        project.task('hybrisYunitinit', type: HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Initializes JUnit tenant for hybris."
            args 'yunitinit'
        }


        project.task('hybrisProduction', type: HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant production' target."
            args 'roduction', '-Dde.hybris.platform.ant.production.skip.build=true', '-Dproduction.legacy.mode=false'
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
            fileset(dir: 'hybris/bin/platform/resources/ant', includes: 'testing.xml')
        }

        project.ant.replaceregexp(match: '\\$\\{HYBRIS_TEMP_DIR\\}\\/junit',
                replace: '${testclasses.junit.temp.directory}',
                flags: 'g',
                byline: true) {
            fileset(dir: 'hybris/bin/platform/resources/ant', includes: 'testing.xml')
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
            fileset(dir: 'hybris/bin/platform/resources/ant', includes: 'testing.xml')
        }

        project.ant.replaceregexp(match: '\\$\\{testclasses.junit.temp.directory\\}',
                replace: '${HYBRIS_TEMP_DIR}/junit',
                flags: 'g',
                byline: true) {
            fileset(dir: 'hybris/bin/platform/resources/ant', includes: 'testing.xml')
        }
    }
}