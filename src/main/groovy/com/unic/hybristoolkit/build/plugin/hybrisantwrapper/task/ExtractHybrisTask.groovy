/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task

import com.unic.hybristoolkit.build.plugin.hybrisantwrapper.HybrisAntWrapperExtension
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject

/**
 * A task to extract a version of hybris, which must be defined as dependency for the 'hybris' configuration.
 * The code was mostly stolen from Gerald Wilhelm's 'fawkes' plugin.
 */
class ExtractHybrisTask extends DefaultTask {

    final Property<File> hybrisZip = project.objects.property(File)

    final HybrisAntWrapperExtension extension

    @Inject
    ExtractHybrisTask() {
        this.extension = project.extensions.hybrisAntWrapper
        outputs.upToDateWhen { new File(extension.hybrisExtractionDir.get(), 'hybris').exists() }
    }

    @TaskAction
    def run() {
        // TODO check for the existence of project.configurations.hybris and log a hint if it's missing.

        logger.quiet(">>> Extracting {} to {}", hybrisZip.get().name, extension.hybrisExtractionDir.get().absolutePath)
        logger.quiet(">>> \tIncluding {}", extension.includeForExtractHybris)
        logger.quiet(">>> \tExcluding {}", extension.excludeForExtractHybris)
        def copyDetails = []
        project.copy {
            from { // use of closure defers evaluation until execution time
                project.zipTree(hybrisZip.get())
            }
            include extension.includeForExtractHybris
            exclude extension.excludeForExtractHybris
            into extension.hybrisExtractionDir.get()

            eachFile { copyDetails << it }
        }

        // restore file dates, see https://issues.gradle.org/browse/GRADLE-2698
        copyDetails.each { FileCopyDetails details ->
            def target = project.file(details.path)
            if (target.exists()) {
                target.setLastModified(details.lastModified)
            }
        }

    }
}