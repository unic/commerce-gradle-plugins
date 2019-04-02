/*
 * Copyright (c) 2019 Unic AG
 */
package com.unic.hybristoolkit.build.plugin.databasedriver

import org.gradle.api.provider.Property

/**
 * Extension class for the DatabaseDriver extension
 */
class DatabaseDriverExtension {
	/**
	 * The dependency string used to fetch the jdbc jar
	 */
	def jdbcDependency

	/**
	 * Defines the jar name for the JDBC driver to be saved as
	 */
	final Property<String> jarName

	DatabaseDriverExtension(project) {
		jarName = project.objects.property(String)
	}
}
