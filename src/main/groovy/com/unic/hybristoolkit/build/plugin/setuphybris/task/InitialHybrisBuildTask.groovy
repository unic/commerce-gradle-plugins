/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.setuphybris.task

import com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task.HybrisAntTask
import org.gradle.api.tasks.TaskAction

/**
 * A task to execute the initial build of the hybris platform, that is required after extracting hybris.
 */
class InitialHybrisBuildTask extends HybrisAntTask {

    InitialHybrisBuildTask() {
        def hybrisConfigDir = new File(project.projectDir, 'hybris/config')
        outputs.upToDateWhen { hybrisConfigDir.exists() }
    }

    def doFirst() {
        if (hybrisConfigDir.exists()) {
            println ">>> Hybris config folder $hybrisConfigDir exists! Skipping initial build."
        } else {
            println ">>> Hybris config folder $hybrisConfigDir does not exists! Running initial build ..."
        }

        def extensionsFile = new File(projectDir, 'hybris/bin/platform//extensions.xml')
        String content = extensionsFile.getText()
        content = content.replaceAll('<extension +name="yempty" +\\/>', '')
        extensionsFile.setText(content)
    }

    @TaskAction
    def run() {
        args 'all'
    }
}
