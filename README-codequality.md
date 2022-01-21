# Hybris Toolkit: Code Quality

## Goal

This plugin supplies tasks and extensions to help with analyzing the code quality.

## Prerequsites

An already configured and ready-to-test hybris instance is required to actually execute the included tasks.

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
