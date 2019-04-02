# Hybris Toolkit: Plugins
This project contains gradle plugins which are meant to be used in other projects, which are set up according to the guidelines described in the [SAP Commerce Cookbook](https://projects.unic.com/display/DCOM/SAP+Commerce+Cookbook).

## Notation

Filenames will be printed in **_bold italics_**.

Special terms will be highlighted with *italics*.

Commands, tasks, variables and code snippets will be printed in a `monospace font`.

```
Code Blocks look like this.
Please note that all commands are samples to be used in a bash shell on a Unix-based OS.
```

*hybris* and *SAP Commerce* are being treated as equivalent terms.

## Contents
This library contains the following plugins. Each plugin has it's own README, which contains more information.

* `com.unic.hybristtoolkit.build.plugin.hybrisantwrapper`

	This is the heart of this project. It basically wraps the ant targets of the hybris buildsystem in gradle and decorates them with some pixiedust. It also helps with downloading, installing and configuring a hybris installation.
	<br/>[read more...](README-hybrisantwrapper.md)

* `com.unic.hybristtoolkit.build.plugin.codequality`

	The codequality plugin wraps the existing test-related tasks to enable us to gather Jacoco execution statistics. It also provides a task to execute a sonar analysis.
	<br/>[read more...](README-codequality.md)


* `com.unic.hybristtoolkit.build.plugin.databasedriver`
	
	The databasedriver plugin installs a given JDBC connector jar into the SAP Commerce ecosystem.
	<br/>[read more...](README-databasedriver.md)
	
	
## Development

*Status: Draft*

### Prerequisites

* Groovy SDK
* Familiarity with Gradle
* IDE (IntelliJ)

### Preparing the plugin(s)

* Adjust some code
* Check the version of the plugin in `gradle.properties` as this will be the version in your local maven repository.
* Build the project and publish it into your local maven repository like so: `./gradlew publishToMavenLocal`

### Testing the plugin(s)

Once you've published the plugins into your local maven repository you need to tell the *consuming project*
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



