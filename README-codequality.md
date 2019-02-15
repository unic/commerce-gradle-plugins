# Hybris Toolkit: hybrisantwrapper

## Goal
This plugin allows you to install a mysqldriver in an existing hybris installation.

## Prerequsites
This plugin requires access to a jysql driver jar in a Maven repository (e.g. Nexus). Als the _com.unic.hybristoolkit.build.plugin.hybrisantwrapper_ needs to be part of the current build, to determine where hybris was installed.

## Configuration
* _mysqlDependency_ - The GAV coordinates that should point to a mysql driver jar.
	<br/>type: String
	<br/>default: `mysql:mysql-connector-java:5.1.18`

## Tasks

* installMysqlDriver - Installs a MySQL driver jar into hybris.