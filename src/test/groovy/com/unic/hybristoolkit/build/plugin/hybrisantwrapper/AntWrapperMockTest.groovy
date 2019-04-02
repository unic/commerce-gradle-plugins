package com.unic.hybristoolkit.build.plugin.hybrisantwrapper

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.ClassRule
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Shared
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class AntWrapperMockTest extends Specification {

    @Rule
    TemporaryFolder testProjectDir = new TemporaryFolder()

    def setup() {
        File buildFile = testProjectDir.newFile('build.gradle')

        buildFile << """
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
}
"""
        File platformDir = testProjectDir.newFolder("hybris", "bin", "platform")
        testProjectDir.newFile("hybris/bin/platform/build.xml") << new File(this.getClass().getResource("/build-hybrismock.xml").toURI()).text
        testProjectDir.newFile("hybris/bin/platform/project.properties")
        testProjectDir.newFolder("hybris","bin","platform","resources","ant")
        testProjectDir.newFile("hybris/bin/platform/resources/ant/testing.xml")


        File antDist = new File(this.getClass().getResource("/apache-ant-1.9.1-hybris.zip").toURI())
        new AntBuilder().unzip(src: antDist, dest: platformDir, overwrite: "true")
    }


    def "Run hybrisClean"() {
        given:

        when:
        def result = runTask('hybrisClean')

        then:
        result.task(":hybrisClean").outcome == SUCCESS
        result.output.contains("[echo] mocking 'clean' target")
    }

    def "Run hybrisAll"() {
        given:

        when:
        def result = runTask('hybrisAll')

        then:
        result.task(":hybrisAll").outcome == SUCCESS
        result.output.contains("[echo] mocking 'all' target")
    }

    def "Run hybrisUnittests"() {
        given:

        when:
        def result = runTask('hybrisUnittests')

        then:
        result.task(":hybrisUnittests").outcome == SUCCESS
        result.output.contains("[echo] mocking 'unittests' target")
    }

    def "Run hybrisWebUnittests"() {
        given:

        when:
        def result = runTask('hybrisWebUnittests')

        then:
        result.task(":hybrisWebUnittests").outcome == SUCCESS
        result.output.contains("[echo] mocking 'allwebtests' target")
    }

    def "Run hybrisIntegrationtests"() {
        given:

        when:
        def result = runTask('hybrisIntegrationtests')

        then:
        result.task(":hybrisIntegrationtests").outcome == SUCCESS
        result.output.contains("[echo] mocking 'integrationtests' target")
    }

    def "Run hybrisWebIntegrationtests"() {
        given:

        when:
        def result = runTask('hybrisWebIntegrationtests')

        then:
        result.task(":hybrisWebIntegrationtests").outcome == SUCCESS
        result.output.contains("[echo] mocking 'allwebtests' target")
    }

    def "Run hybrisAlltests"() {
        given:

        when:
        def result = runTask('hybrisAlltests')

        then:
        result.task(":hybrisAlltests").outcome == SUCCESS
        result.output.contains("[echo] mocking 'alltests' target")
    }

    def "Run hybrisInitialize"() {
        given:

        when:
        def result = runTask('hybrisInitialize')

        then:
        result.task(":hybrisInitialize").outcome == SUCCESS
        result.output.contains("[echo] mocking 'initialize' target")
    }

    def "Run hybrisYunitinit"() {
        given:

        when:
        def result = runTask('hybrisYunitinit')

        then:
        result.task(":hybrisYunitinit").outcome == SUCCESS
        result.output.contains("[echo] mocking 'yunitinit' target")
    }

    def "Run hybrisProduction"() {
        given:

        when:
        def result = runTask('hybrisProduction')

        then:
        result.task(":hybrisProduction").outcome == SUCCESS
        result.output.contains("[echo] mocking 'production' target")
    }


    private BuildResult runTask(String taskName) {
        GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments(taskName)
                .withPluginClasspath()
                .build()
    }

    def "hybrisAll"() {}

    def "hybrisProduction"() {}
}