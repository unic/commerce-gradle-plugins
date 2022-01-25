# Unic commerce-gradle-plugins: Code Quality

## Goal

This plugin supplies tasks and extensions to help with analyzing the code quality.

## Prerequsites

An already configured and ready-to-test SAP Commerce instance is required to actually execute the included tasks.

## Configuration

- _jacocoDependency_ - The GAV coordinates that should point to a jacoco agent jar.
  <br/>type: String
  <br/>default: `org.jacoco:org.jacoco.agent:0.8.7:runtime`

- _jacocoCliDependency_ - The GAV coordinates that should point to a jacoco cli jar.
  <br/>type: String
  <br/>default: `org.jacoco:org.jacoco.cli:0.8.7:nodeps@jar`

- _sonarrunnerDependency_ - The GAV coordinates that should point to a sonarrunner agent jar.
  <br/>type: String
  <br/>default: `org.sonarsource.scanner.cli:sonar-scanner-cli:4.6.2.2472@jar`

- _jacocoTargets_ - The tasks to enhance by enabling jacoco instrumentation.
  <br/>type: String[]
  <br/>default: `['cloudTests','cloudWebTests']`

## Tasks

- sonar - Executes a sonar analysis.
- jacocoReport - Generates a XML report of all available jacoco .exec files.

## Extensions

### Coverage data

To generate coverage data you need to configure the tasks that need to be enhanced in order to enable instrumentalization by the jacoco agent. You can do this by setting the jacocoTargets configuration value.

The task name is being used to generate a target file name for the jacoco execution data. The following pattern will be used: `${projectDir}/hybris/log/jacoco/jacoco-${classifier}.exec`.

### Coverage reports

In order for sonar to be able to pick up the reports, you may have to compile the jacoco execution data into an XML report. Call the `jacocoReport` target to do so before executing the `sonar` task.

## Restrictions / Notes

Currently the jacoco related extensions are assuming that hybris is installed in `${projectDir}/hybris`.


## Development

_Status: Draft_

Deprecation warning: Please note that both the `hybrisantwrapper` and the `databasedriver` have been deprecated in favor of the official (SAP commerce-gradle-plugin)[https://github.com/SAP/commerce-gradle-plugin/] starting with v0.0.12 any may be removed in any later version.

This plugin can be combined with the SAP plugins without any issues.

### Prerequisites

- Groovy SDK
- Familiarity with Gradle
- IDE (VS Code, IntelliJ)

### Preparing the plugin(s)

- Adjust some code
- Check the version of the plugin in `gradle.properties` as this will be the version in your local maven repository.
- Build the project and publish it into your local maven repository like so: `./gradlew publishToMavenLocal`

### Testing the plugin(s)

Once you've published the plugins into your local maven repository you need to tell the _consuming project_
that you want to load the plugins from your local maven repository instead of nexus.

You can do so by adjusting the `settings.gradle` in the `hybris-toolkit-build` repository:

```
pluginManagement {
    repositories {
        mavenLocal()
        [...]
    }
}

rootProject.name = 'hybris-toolkit'
```

Make sure the `mavenLocal()` is the first line in the `repositories` section as otherwise you will run into
very strange errors.
