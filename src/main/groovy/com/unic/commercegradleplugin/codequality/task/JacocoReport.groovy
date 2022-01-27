/*
 * Copyright (c) 2022 Unic AG
 */

package com.unic.commercegradleplugin.codequality.task

import groovy.io.FileType
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskAction

/**
 * Finds all .exec files in the hybris jacoco output dir and executes jacoco-cli to generate an XML report from those files.
 */
class JacocoReport extends DefaultTask {

    final Property<FileCollection> jacocoCliClasspath = project.objects.property(FileCollection)
    def jacocoCliWorkingDir
    File hybrisDir = new File("${project.projectDir}/hybris")

    @TaskAction
    void sonarrunner() {
        File customCodeDir = new File(hybrisDir, "/bin/custom")
        def classDirs = []
        customCodeDir.eachFileRecurse(FileType.DIRECTORIES) { dir ->
            if (dir.name == 'classes') {
                classDirs << "--classfiles=${dir}"
            }
        }

        File jacocoLogDir = new File(hybrisDir, "/log/jacoco")
        def execFiles = []
        jacocoLogDir.eachFileRecurse(FileType.FILES) { file ->
            if (file.name.endsWith('.exec')) {
                execFiles << file
            }
        }
        project.javaexec {
            classpath = jacocoCliClasspath.get()
            main = 'org.jacoco.cli.internal.Main'
            workingDir = jacocoCliWorkingDir
            systemProperty 'java.awt.headles', 'true'
            args 'report'
            args execFiles
            args "--xml=${jacocoLogDir}/jacoco-report.xml"
            args classDirs
        }
    }
}
