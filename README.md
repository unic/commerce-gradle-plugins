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


* `com.unic.hybristtoolkit.build.plugin.installmysqldriver`
	
	Like the name says it installs the mysql driver into a hybris installation.
	<br/>[read more...](README-installmysqldriver.md)


