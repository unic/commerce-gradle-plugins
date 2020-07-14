package com.unic.hybristoolkit.build.plugin.hybrisantwrapper


import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class DownloadIntegrationDependenciesTest extends Specification {

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
    hybrisDependency = 'de.hybris.platform:sap-commerce-cloud:20.05.00@zip'
    
    integrationDependencies = ["de.hybris.platform:hybris-commerce-integrations:20.05.00@zip", "de.hybris.platform:hybris-datahub-integration-suite:20.05.00@zip"]
}
"""

    }

    def "extractHybris"() {
        given:

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('extractHybris')
                .withPluginClasspath()
                .build()

        then:
        result.task(":extractHybris").outcome == SUCCESS

        testProjectDir.getRoot().toPath().resolve("hybris").toFile().exists()
        testProjectDir.getRoot().toPath().resolve("hybris/bin/modules/sap-availability").toFile().exists()
        testProjectDir.getRoot().toPath().resolve("hybris/bin/ext-integration").toFile().exists()
    }

}
