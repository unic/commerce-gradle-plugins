/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.setuphybris.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.tasks.TaskAction

/**
 * A task to extract a version of hybris, which must be defined as dependency for the 'hybris' configuration.
 * The code was mostly stolen from Gerald Wilhelm's 'fawkes' plugin.
 */
class ExtractHybrisTask extends DefaultTask {

    ExtractHybrisTask() {
        def hybrisDir = new File(project.projectDir, "hybris");
        outputs.upToDateWhen { hybrisDir.exists() }
    }

    @TaskAction
    def run() {
        // TODO check for the existence of project.configurations.hybris and log a hint if it's missing.

        logger.quiet(">>> Extracting {} to {}", project.configurations.hybris.collect { it.name }, project.projectDir)
        logger.quiet(">>> \tIncluding {}", project.setupHybris.includeForExtractHybris)
        logger.quiet(">>> \tExcluding {}", project.setupHybris.excludeForExtractHybris)
        def copyDetails = []
        project.copy {
            from { // use of closure defers evaluation until execution time
                project.configurations.hybris.collect { project.zipTree(it) }
            }
            include project.setupHybris.includeForExtractHybris
            exclude project.setupHybris.excludeForExtractHybris
            into project.projectDir

            eachFile { copyDetails << it }
        }

        // restore file dates, see https://issues.gradle.org/browse/GRADLE-2698
        copyDetails.each { FileCopyDetails details ->
            def target = new File(project.projectDir, details.path)
            if (target.exists()) {
                target.setLastModified(details.lastModified)
            }
        }

    }
}