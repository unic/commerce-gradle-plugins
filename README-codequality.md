# Hybris Toolkit: Code Quality

## Goal
This plugin supplies tasks and extensions to help with analyzing the code quality. 

## Prerequsites
An already configured and ready-to-test hybris instance is required to actually execute the included tasks. 

## Configuration
* _jacocoDependency_ - The GAV coordinates that should point to a jacoco agent jar.
	<br/>type: String
	<br/>default: `org.jacoco:org.jacoco.agent:0.7.7.201606060606:runtime`

* _sonarrunnerDependency_ - The GAV coordinates that should point to a sonarrunner agent jar.
	<br/>type: String
	<br/>default: `org.sonarsource.scanner.cli:sonar-scanner-cli:3.1.0.1141@jar`

## Tasks
* sonar - Executes a sonar analysis.

## Extensions
### Coverage reports
The following targets are being enhanced, so they generate jacoco reports:

| Task                      | Report file classifier|
|---                        |---                    |
| hybrisUnittests           | unittests             |
| hybrisIntegrationtests    | integrationtests      |
| hybrisWebUnittests        | webunittests          |
| hybrisWebIntegrationtests | webintegrationtests   |
| hybrisAlltests            | alltests              |

The report file classifier is being used to generate a target file name for the jacoco execution report. The following pattern will be used: `${projectDir}/hybris/log/jacoco/jacoco-${classifier}.exec`

## Restrictions / Notes
Currently the jacoco related extensions are assuming, that hybris is installed in `${projectDir}/hybris`.