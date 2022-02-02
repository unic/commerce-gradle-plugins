# Unic commerce-gradle-plugins: Code Quality

## Goal

This plugin supplies tasks and extensions to help with analyzing the code quality of a SAP Commerce project.

Since such projects cannot rely on the common sonarqube and jacoco plugins for technical reasons, a plugin like this one is required to do the SCA.

### History

Originally Unic had their own toolchain to build, test, analyze, deploy and configure applications in their own and their customer's hosting environment. As SAP presented the Commerce Cloud v2 environment the toolchain became less relevant and the deploy & configure components have been deprecated. With the introduction of the [commerce-gradle-plugin](https://github.com/SAP/commerce-gradle-plugin), which seems to share some ideas with the Unic toolchain, the build and test components were deprecated, too.

So far the `codequality` component has survived.

## Prerequsites

An already configured and ready-to-test SAP Commerce instance is required to actually execute the included tasks.


## Usage

Add this to your build.gradle.ks to enable the plugin:

```kotlin
plugins {
    id("com.unic.commercegradleplugin.codequality") version("0.0.1")
}
apply<CodeQualityPlugin>()
```

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

## Tasks / Rules

### _jacoco\<targettask>_ Rule

To generate coverage data you need to configure the tasks that need to be enhanced in order to enable fon-the-fly instrumentation by the jacoco agent.

This tasks wraps any _target task_ with supplemental tasks, which reconfigure a hybris installation to enable or disable jacoco on-the-fly instrumentation to generate .exec files before or after running the actual _target task_, respectively. The _target task_ name is being used to generate a target file name for the jacoco execution data. The following pattern will be used: `${projectDir}/hybris/log/jacoco/jacoco-${classifier}.exec`.

### _jacocoReport_ Task

In order for sonar to be able to pick up the reports, you will have to compile the jacoco execution data into an XML report. Call the `jacocoReport` target to do so before executing the `sonar` task, for example by defining a dependency.

### _sonar_ Task

This tasks executed the Sonar Runner to analyze your code and push a new report to Sonarqube.
## Restrictions / Notes

Currently the jacoco related extensions are assuming that hybris is installed in `${projectDir}/hybris`.
## Development

_Status: Draft_

This plugin is meant to be used to supplement the (SAP commerce-gradle-plugin)[https://github.com/SAP/commerce-gradle-plugin/] plugin suite. Please make sure to stay compatible.

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
