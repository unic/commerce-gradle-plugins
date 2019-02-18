package com.unic.hybristoolkit.build.plugin.hybrisantwrapper

import org.apache.groovy.util.Maps
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class DownloadBuildConfigureTest extends Specification {

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

    }

    def "configureHybris"() {
        given:
        testProjectDir.newFolder("config", "commonConfig", "config")
        testProjectDir.newFolder("config", "subdir", "specificConfig", "config", "sublevel")
        testProjectDir.newFolder("config", "subdir", "irrelevantDir")
        testProjectDir.newFolder("config", "subdir", "emptyConfig", "config")
        testProjectDir.newFile("config/commonConfig/config/local.properties") << "#commonConfig\n"
        testProjectDir.newFile("config/commonConfig/config/common.file") << "foobar\n"
        testProjectDir.newFile("config/subdir/specificConfig/config/local.properties") << "#specificConfig\n"
        testProjectDir.newFile("config/subdir/specificConfig/config/specific.file") << "barfoo\n"
        testProjectDir.newFile("config/subdir/specificConfig/config/sublevel/morespecific.file") << "booyakka\n"

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('configureHybris')
                .withPluginClasspath()
                .withEnvironment(Maps.of("UNIC_TK_V2_CONFIG_PROFILES", "commonConfig,subdir/specificConfig,subdir/emptyConfig,path/to/nonexisting/directory"))
                .build()

        then:
        result.task(":extractHybris").outcome == SUCCESS
        result.task(":initialHybrisBuild").outcome == SUCCESS
        result.task(":configureHybris").outcome == SUCCESS

        testProjectDir.getRoot().toPath().resolve("hybris").toFile().exists()

        // Check that local.properties contains properties from all active profiles
        File localpropertiesFile = testProjectDir.getRoot().toPath().resolve("hybris/config/local.properties").toFile()
        localpropertiesFile.text =~ /(?m)^#commonConfig$/
        localpropertiesFile.text =~ /(?m)^#specificConfig$/

        // Check that files from the active profiles were copied
        testProjectDir.getRoot().toPath().resolve("hybris/config/common.file").toFile().text =~ /(?m)^foobar$/
        testProjectDir.getRoot().toPath().resolve("hybris/config/specific.file").toFile().text =~ /(?m)^barfoo$/
        testProjectDir.getRoot().toPath().resolve("hybris/config/sublevel/morespecific.file").toFile().text =~ /(?m)^booyakka$/

    }

}