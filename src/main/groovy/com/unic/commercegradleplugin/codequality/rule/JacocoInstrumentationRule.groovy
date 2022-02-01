package com.unic.commercegradleplugin.codequality.rule

import org.gradle.api.Project
import org.gradle.api.Rule
import org.gradle.api.Task
import org.gradle.api.provider.Property

class JacocoInstrumentationRule implements Rule {

    private static final String PREFIX = "jacoco";
    private final Project project;
    private final Property<File> jacocoJar
    private File hybrisDir

    JacocoInstrumentationRule(Project project, File hybrisDir) {
        this.hybrisDir = hybrisDir
        this.project = project;
        this.jacocoJar = project.objects.property(File)
    }

    @Override
    String getDescription() {
        return "Pattern: jacoco<target>: Wrap Jacoco instrumentation tasks around <target>";
    }

    @Override
    void apply(String taskName) {
        if (taskName.startsWith(PREFIX)) {
            String targetTask = (taskName - PREFIX).uncapitalize()
            project.task(taskName) {
                doLast { enableJacoco(targetTask) }
                finalizedBy targetTask
            }

            Task cleanupTask = project.task(taskName + 'Cleanup') {
                doLast { disableJacoco() }
            }

            project.tasks.named(targetTask) {
                finalizedBy cleanupTask
            }
        }
    }


    def enableJacoco(String classifier) {
        println("BEWARE: Appending Jacoco configuration for ${classifier} tests to 'standalone.javaoptions'!")
        File destdir = new File(hybrisDir, "log/jacoco/")
        File destfile = destdir.toPath().resolve("jacoco-${classifier}.exec").toFile()
        println("Jacoco report output file: ${destfile}")
        GString args = "-DjacocoConf=start -javaagent:${jacocoJar.get()}=destfile=${destfile},append=true,excludes=*Test -DjacocoConf=end"

        String config = getConfigFile().text
        if ((~/(?ms).*^standalone\.javaoptions=.*?$.*/).matcher(config).matches()) {
            getConfigFile().text = config.replaceAll(/(?m)^(standalone\.javaoptions=.*?)(-DjacocoConf=start.*-DjacocoConf=end)?$/, '$1 ' + args)
        } else {
            getConfigFile().text = config + "\n" + "standalone.javaoptions=-DjacocoConf=fulldelete " + args
        }
    }

    def disableJacoco() {
        println("BEWARE: Removing Jacoco configuration from 'standalone.javaoptions'")

        getConfigFile().text = getConfigFile().text.replaceAll(/(?m)^(standalone\.javaoptions=.*?)(.-DjacocoConf=start.*-DjacocoConf=end)?$/, '$1')
        getConfigFile().text = getConfigFile().text.replaceAll(/(?m)^\nstandalone\.javaoptions=-DjacocoConf=fulldelete.*$/, '')
    }

    def getConfigFile() {
        new File(hybrisDir, 'config/local.properties')
    }
}
