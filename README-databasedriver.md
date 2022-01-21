# Hybris Toolkit: Database Driver

**_< DEPRECATED >_**

## Goal

This plugin allows you to install a database driver in an existing SAP Commerce installation.

## Prerequisites

- This plugin requires access to a JDBC driver jar (like mysql) in a Maven repository (e.g. Nexus)
- The _com.unic.hybristoolkit.build.plugin.hybrisantwrapper_ plugin needs to be part of the current build
  in order to be able to determine the hybris directory.

## Configuration

- _jdbcDependency_ - The GAV coordinates that should point to a JDBC driver jar.
  <br/>type: String
  <br/>default: none
  <br/>example: `mysql:mysql-connector-java:5.1.18`
- _jarName_ - The name of the resulting JAR in the hybris platform directory.
  <br/>type: String
  <br/>default: none
  <br/>example: `mysql-connector-java.jar`

## Tasks

- installDatabaseDriver - Installs a JDBC driver jar into hybris.
