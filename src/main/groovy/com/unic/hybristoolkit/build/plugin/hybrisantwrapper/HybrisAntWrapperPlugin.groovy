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
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant unittests' target."
            args 'unittests'
        }

        project.task('hybrisIntegrationtests', type: HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant integrationtests' target."
            args 'integrationtests'
        }

        project.task('hybrisAlltests', type: HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant alltests' target."
            args 'alltests'
        }

        project.task('hybrisAllwebtests', type: HybrisAntTask) {
            group 'Unic Hybris Toolkit - Wrapped Tasks'
            description "Runs 'ant allwebtests' target."
            args 'allwebtests'
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


}