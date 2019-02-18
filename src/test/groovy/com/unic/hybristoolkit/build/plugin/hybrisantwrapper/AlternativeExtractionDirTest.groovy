package com.unic.hybristoolkit.build.plugin.hybrisantwrapper

import com.google.common.io.PatternFilenameFilter
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class AlternativeExtractionDirTest extends Specification {

    @Rule
    TemporaryFolder testProjectDir = new TemporaryFolder()

    File settingsFile
    File buildFile
    String buildscriptBase

    def setup() {
        settingsFile = testProjectDir.newFile('settings.gradle')
        buildFile = testProjectDir.newFile('build.gradle')

        buildscriptBase = """
plugins {
    id 'com.unic.hybristoolkit.build.plugin.hybrisantwrapper'
}
repositories {
    maven {
        credentials { 
            username System.getenv('NEXUS_USER')
            password System.getenv('NEXUS_PASSWORD')
        }
        url "https://nexus.unic.com/nexus/repository/thirdparty/"
    }
    mavenCentral()
}
hybrisAntWrapper{
    hybrisDependency = 'de.hybris.platform:sap-commerce-cloud:18.08@zip'
    hybrisExtractionDir = project.file('extraction_test')
    includeForExtractHybris = ['hybris','installer']
}
"""

        buildFile << buildscriptBase
    }

    def "extractHybris with custom configuration"() {
        given:

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('extractHybris')
                .withPluginClasspath()
                .build()

        then:
        result.output.contains("Extracting sap-commerce-cloud-18.08.zip to ${testProjectDir.getRoot().toPath().toRealPath()}/extraction_test")
        result.task(":extractHybris").outcome == SUCCESS

        // check for the extraction_test target directory
        testProjectDir.getRoot().listFiles(new PatternFilenameFilter("extraction_test")).size() == 1

        // Check the expected number of items has been extracted and that they can be found by their names
        testProjectDir.getRoot().toPath().resolve("extraction_test").toFile().listFiles().size() == 2
        testProjectDir.getRoot().toPath().resolve("extraction_test/hybris").toFile().exists()
        testProjectDir.getRoot().toPath().resolve("extraction_test/installer").toFile().exists()
    }
}