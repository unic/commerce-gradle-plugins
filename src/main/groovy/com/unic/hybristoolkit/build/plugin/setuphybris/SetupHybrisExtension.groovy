/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.setuphybris

/**
 * Extension class for the SetupHybris extension
 */
class SetupHybrisExtension {
    List includeForExtractHybris = ['hybris/**']
    List excludeForExtractHybris = []
    /**
     * The depencency string used to fetch the hybris zip.
     */
    def hybrisDependency = 'de.hybris.platform:hybris-commerce-suite:6.6.0.8@zip'

}
