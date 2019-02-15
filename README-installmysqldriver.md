# Hybris Toolkit: codequality

## Goal
This plugin allows you to install a mysqldriver in an existing hybris installation.

## Prerequsites
This plugin requires access to a jysql driver jar in a Maven repository (e.g. Nexus). Als the _com.unic.hybristoolkit.build.plugin.hybrisantwrapper_ needs to be part of the current build, to determine where hybris was installed.

## Configuration
* _jacocoDependency_ - The GAV coordinates that should point to a jacoco agent jar.
	<br/>type: String
	<br/>default: `org.jacoco:org.jacoco.agent:0.7.7.201606060606:runtime`

* _sonarrunnerDependency_ - The GAV coordinates that should point to a sonarrunner agent jar.
	<br/>type: String
	<br/>default: `org.sonarsource.scanner.cli:sonar-scanner-cli:3.1.0.1141@jar`
	
* _jacocoDependency_ - The GAV coordinates that should point to a jacoco agent jar.
	<br/>type: String
	<br/>default: `org.jacoco:org.jacoco.agent:0.7.7.201606060606:runtime`

## Tasks

* sonar - Executes a sonar analysis.