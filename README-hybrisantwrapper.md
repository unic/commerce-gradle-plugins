# Hybris Toolkit: hybrisantwrapper

## Goal
This plugin aims at providing the most important tasks for setting up, configuring and building a hybris installation, in order to get it running locally or in a CI environment and package it for deployment.

## Prerequsites
This plugin requires access to a hybris distribution zip in a Maven repository (e.g. Nexus).

## Configuration
* _hybrisDependency_ - The GAV coordinates that should point to a hybris distribution zip.
	<br/>type: String
	<br/>default: `de.hybris.platform:sap-commerce-cloud:18.11@zip`

* _includeForExtractHybris_ - Includes to consider when extracting hybris.
	<br/>type: List<String>
	<br/>default: `['hybris/**']`

* _includeForExtractHybris_ - Excludes to consider when extracting hybris.
	<br/>type: List<String>
	<br/>default: `[]`

* _hybrisExtractionDir_ - Where to extract the hybris zip.
	<br/>type: File
	<br/>default: `${project.projectDir}`
	
* _hybrisConfigurationDir_ - The path where configuration profiles can be found.
    <br/>type: String
	<br/>default: `config`
	
* _testpackages_ - The packages that shall be included when executing tests.
	<br/>type: List<String>
	<br/>default: `['com.unic.*']`
	
* _defaultConfigProfile_ - The default configuration profile that should always be used.
    <br/>type: String
	<br/>default: `common`
	
* _perConfigSubdirectory_ - If the relevant configuration is stored in a sub-directory (or relative path) per profile, then set this value. Leave empty otherwise.
    <br/>type: String
	<br/>default: `` (empty String)

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

## Configuration
A developer can control which files from the `${hybrisConfigurationDir}` will be considered, when the plugin's task `configureHybris` is being executed to generate a local configuration.

Do to so he or she must export an environment variable called `UNIC_TK_V2_CONFIG_PROFILES` that contains a comma-seperated list of paths relative to `${hybrisConfigurationDir}`. An example:

    export UNIC_TK_V2_CONFIG_PROFILES=developerCommon,developers/some.user`
    
This list is prepended with the default configuration directory in `${defaultConfigProfile}`. Effectively this will cause the following directories to be considered:

* **_${project.projectDir}/config/common_**
* **_${project.projectDir}/config/developerCommon_**
* **_${project.projectDir}/config/developers/some.user_**

By setting the plugin's configuration property `${perConfigSubdirectory}` you can even define a subdirectory per list entry. Setting it to the value `conf` for example will lead to this list of effectively considered directories:  

* **_${project.projectDir}/config/common/conf_**
* **_${project.projectDir}/config/developerCommon/conf_**
* **_${project.projectDir}/config/developers/some.user/conf_**
