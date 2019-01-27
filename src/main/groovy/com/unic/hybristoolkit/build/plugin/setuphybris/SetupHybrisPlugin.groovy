/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.setuphybris

import com.unic.hybristoolkit.build.plugin.setuphybris.task.ConfigureHybrisTask
import com.unic.hybristoolkit.build.plugin.setuphybris.task.ExtractHybrisTask
import com.unic.hybristoolkit.build.plugin.setuphybris.task.InitialHybrisBuildTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A Plugin to manage all steps required to download hybris, build it initialy and configure it, to get it ready for a build.
 */
class SetupHybrisPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.create('setupHybris', SetupHybrisExtension)

        def extractHybrisTask = project.task('extractHybris', type: ExtractHybrisTask) {
            group "Unic Hybris Toolkit"
            description = 'Extracts hybris.'
        }

        def initialHybrisBuildTask = project.task('initialHybrisBuild', type: InitialHybrisBuildTask) {
            group "Unic Hybris Toolkit"
            description = 'If no config folder exists yet this task runs an initial build to create all necessary folders.'
        }

        def configureHybrisTask = project.task("configureHybris", type: ConfigureHybrisTask) {
            group "Unic Hybris Toolkit"
            description "Sets up configuration (local.properties, localextensions.xml) and copies ci folder to hybris/config and customize folder to hybris/bin."
        }

        initialHybrisBuildTask.dependsOn(extractHybrisTask)
        configureHybrisTask.dependsOn(initialHybrisBuildTask)
    }
}
