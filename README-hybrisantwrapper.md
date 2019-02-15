# Hybris Toolkit: hybrisantwrapper

## Goal
This plugin aims at providing the most important tasks for setting up, configuring and building a hybris installation, in order to get it running locally or in a CI environment and package it for deployment.

## Prerequsites
This plugin requires access to a hybris distribution zip in a Maven repository (e.g. Nexus).

## Configuration
* _hybrisDependency_ - The GAV coordinates that should point to a hybris distribution zip.
	<br/>type: String
	<br/>default: `de.hybris.platform:sap-commerce-cloud:18.11@zip`

* _hybrisInstallationPath_ - the location of the hybris installation to manage (absolute or relative to `projectDir`).
	<br/>type: String
	<br/>default: `hybris`

* _includeForExtractHybris_ - Includes to consider when extracting hybris.
	<br/>type: List<String>
	<br/>default: `['hybris/**']`

* _includeForExtractHybris_ - Excludes to consider when extracting hybris.
	<br/>type: List<String>
	<br/>default: `[]`
	
* _testpackages_ - the packages to to include when executing tests.
	<br/>type: List<String>
	<br/>default: `['com.unic.*']`

## Tasks

* extractHybris - Extracts hybris to the specified target
* initialHybrisBuild - If no config folder exists yet this task runs an initial build to create all necessary folders.
* configureHybris - Merges local configuration and customizations with hybris installation.
* hybrisAll - Runs 'ant all' target.
* hybrisAlltests - Runs 'ant alltests' target.
* hybrisClean - Runs 'ant clean' target.
* hybrisInitialize - Runs 'ant initialize' to initialize the hybris master tenant.
* hybrisIntegrationtests - Runs 'ant integrationtests' target.
* hybrisProduction - Runs 'ant production' target.
* hybrisUnittests - Runs 'ant unittests' target.
* hybrisWebIntegrationtests - Runs 'ant allwebtests' target with an annotation-limitation for integrationtests.
* hybrisWebUnittests - Runs 'ant allwebtests' target with an annotation-limitation for unittests.
* hybrisYunitinit - Runs 'ant initialize' to initialize the hybris junit tenant.