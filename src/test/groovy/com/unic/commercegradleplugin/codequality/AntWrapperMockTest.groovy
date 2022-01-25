package com.unic.commercegradleplugin.codequality

import org.gradle.api.Task
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import spock.lang.Specification


class JacocoRuleTest extends Specification {

    @Rule
    private TemporaryFolder testProjectDir = new TemporaryFolder()

    private File configDir;
    void setup() {
        File buildFile = testProjectDir.newFile('build.gradle')

        buildFile << """
plugins {
    id 'com.unic.commercegradleplugin.codequality'
}
repositories {
    mavenCentral()
}
tasks.register('propsEcho') {
    doLast {
        println 'propsEcho:'+file('hybris/config/local.properties').text
    }
}
"""
        configDir = testProjectDir.newFolder('hybris', 'config')
    }

    @Test
    void antRuleDecoration() {
        given:
        File localprops = new File(configDir,'local.properties')
        localprops.createNewFile()

        when:
        BuildResult result = runTask('jacocoPropsEcho')

        then:
        result.task(':jacocoPropsEcho').outcome == TaskOutcome.SUCCESS
        result.task(':propsEcho').outcome == TaskOutcome.SUCCESS
        result.task(':jacocoPropsEchoCleanup').outcome == TaskOutcome.SUCCESS
    }

    @Test
    void localpropsEmpty() {
        given:
        File localprops = new File(configDir,'local.properties')
        localprops.text = ""

        when:
        BuildResult result = runTask('jacocoPropsEcho')


        then:
        (~/(?ms).*propsEcho:\nstandalone\.javaoptions=-DjacocoConf=fulldelete -DjacocoConf=start.*-DjacocoConf=end.*/)
                .matcher(result.output).matches()
        localprops.text.isEmpty()
    }

    @Test
    void existingLocalProps() {
        given:
        File localprops = new File(configDir,'local.properties')
        String text = """
foo=bar
standalone.javaoptions=-Xmx42g
boo=yakka

"""
        localprops.text = text

        when:
        BuildResult result = runTask('jacocoPropsEcho')

        then:
        (~/(?ms).*propsEcho:\nfoo=bar\nstandalone\.javaoptions=-Xmx42g -DjacocoConf=start.*-DjacocoConf=end\nboo=yakka.*/).matcher(result.output).matches()
        localprops.text == text
    }


    private BuildResult runTask(String taskName) {
        return GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments(taskName)
                .withPluginClasspath()
                .build()
    }

}