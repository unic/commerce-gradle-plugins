/*
 * Copyright (c) 2019 Unic AG
 */

package com.unic.hybristoolkit.build.plugin.hybrisantwrapper.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * This task configures an existing hybris installation. It requires a directory containing the commerce suite in ${projectDir}/hybris.
 * The following will happen:
 * <ul>
 *     <li>Copy localextensions.xml to ${projectDir}/hybris/config</li>
 *     <li>Copy/merge local.properties ${projectDir}/hybris/config</li>
 *     <li>Copy over all files from customize into ${projectDir}/hybris, replacing existing ones.</li>
 * </ul>
 * <p>By default the configuration from the $projectDir/config/common directory will be picked for copying (see defaultConfigProfile).
 * If the user has the env- variable <pre>CUSTOM_CONFIG_PROFILE<pre> set, the configuration files from ${projectDir}/config/${CUSTOM_CONFIG_PROFILE}* take precedence and replace (in case of localextensions.xml) or complement the default ones (customize and local.properties).
 * The custom configuration shall always be able to override the common configuration.</p>
 * <p><pre>CUSTOM_CONFIG_PROFILE<pre> may also contain a comma seperated lists of profile names. The profiles to the right have a higher precedence.</p>
 */
class ConfigureHybrisTask extends DefaultTask {
// TODO add input, output or internal annotation

    def defaultConfigProfile
    def perConfigSubdirectory
    def hybrisExtractionDir
    def hybrisConfigurationDir

    List<String> configProfiles = []

    ConfigureHybrisTask() {
        super()
        inputs.dir { hybrisConfigurationDir.get() }
    }

    @TaskAction
    def run() {
        // determine which profiles are active
        configProfiles = [defaultConfigProfile.get()]
        if (System.getenv('UNIC_TK_V2_CONFIG_PROFILES')) {
            configProfiles.addAll(System.getenv('UNIC_TK_V2_CONFIG_PROFILES').split(","))
        } else if (System.getenv('CUSTOM_CONFIG_PROFILE')) {
            configProfiles.addAll(System.getenv('CUSTOM_CONFIG_PROFILE').split(","))
        }

        // customize
        configProfiles.each {
            safeCopy(new File(hybrisConfigurationDir.get(),"${it}${slashPrefixedPerConfigSubdirectory()}"), new File(hybrisExtractionDir.get(), 'hybris'))
        }

        // merge local.properties
        mergePropertiesWithFallback(configProfiles, new File(hybrisExtractionDir.get(), 'hybris/config/local.properties'))
    }

    def safeCopy(source, targetDir) {
        if (source.exists()) {
            logger.quiet(">>> copy: $source >> $targetDir")
            project.copy {
                from source
                into targetDir
                exclude 'config/local.properties'
            }
        } else {
            logger.quiet(">>> copy: Ignoring non-existing source $source !")
        }
    }


    def mergePropertiesWithFallback(profiles, targetFile) {
        logger.quiet(">>> merging profiles ${profiles} into ${targetFile}")

        targetFile.text = profiles.collect {
            def propSrc = new File(hybrisConfigurationDir.get(),"/${it}${slashPrefixedPerConfigSubdirectory()}/config/local.properties")
            logger.quiet(">>> > file ${propSrc} ${propSrc.exists()?'found':'not found'}")
            propSrc.exists() ? propSrc.text : ''
        }.join("\n\n")
    }

    def slashPrefixedPerConfigSubdirectory(){
        "${File.separator}${perConfigSubdirectory.get()}"
    }
}
