/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.hybrisantwrapper

import org.gradle.api.provider.Property

/**
 * An extension for the HybrisAntWrapper plugin.
 */
class HybrisAntWrapperExtension {

    /**
     * The depencency string used to fetch the hybris zip.
     */
    def hybrisDependency = 'de.hybris.platform:hybris-commerce-suite:6.6.0.8@zip'

    /**
     * Inclusions and excluions for the extraction of the hybris zip.
     */
    List includeForExtractHybris = ['hybris/**']
    List excludeForExtractHybris = []

    /**
     * The extraction path for the hybris zip.
     */
    final Property<File> hybrisExtractionDir

    /**
     * The path where configuration profiles can be found.
     */
    final Property<File> hybrisConfigurationDir

    /**
     * The packages that shall be included when executing tests.
     */
    final Property<String> testpackages

    /**
     * The default configuration profile that should always be used
     */
    final Property<String> defaultConfigProfile

    /**
     * If the relevant configuration is stored in a sub-directory (or relative path) per profile, then set this value.
     * Leave empty otherwise.
     */
    final Property<String> perConfigSubdirectory

    HybrisAntWrapperExtension(project) {
        testpackages = project.objects.property(String)
        testpackages.set("com.unic.*")
        hybrisExtractionDir = project.objects.property(File)
        hybrisExtractionDir.set(project.projectDir)
        hybrisConfigurationDir = project.objects.property(File)
        hybrisConfigurationDir.set(project.file('config'))
        defaultConfigProfile = project.objects.property(String)
        defaultConfigProfile.set("common")
        perConfigSubdirectory = project.objects.property(String)
    }
}
