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
     * The packages that shall be included when executing tests.
     */
    final Property<String> testpackages

    HybrisAntWrapperExtension(project) {
        testpackages = project.objects.property(String)
        testpackages.set("com.unic.*")
    }
}
