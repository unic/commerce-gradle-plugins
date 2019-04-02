/*
 * Copyright (c) 2019 Unic AG
 */
package com.unic.hybristoolkit.build.plugin.installdatabasedriver

import org.gradle.api.provider.Property

/**
 * Extension class for the InstallDatabaseDriver extension
 */
class InstallDatabaseDriverExtension {
	/**
	 * The dependency string used to fetch the jdbc jar
	 */
	def jdbcDependency

	/**
	 * Defines the jar name for the JDBC driver to be saved as
	 */
	final Property<String> jarName

	InstallDatabaseDriverExtension(project) {
		jarName = project.objects.property(String)
	}
}
