/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task


import com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task.HybrisAntTask

/**
 * A task to execute the initial build of the hybris platform, that is required after extracting hybris.
 */
class InitialHybrisBuildTask extends HybrisAntTask {

    InitialHybrisBuildTask() {
        outputs.upToDateWhen { new File(hybrisExtractionDir.get(), 'hybris/config').exists() }
        arguments = ['all']
    }

    def doFirst() {
        def extensionsFile = new File(hybrisExtractionDir.get(), 'hybris/bin/platform//extensions.xml')
        String content = extensionsFile.getText()
        content = content.replaceAll('<extension +name="yempty" +\\/>', '')
        extensionsFile.setText(content)
    }
}
