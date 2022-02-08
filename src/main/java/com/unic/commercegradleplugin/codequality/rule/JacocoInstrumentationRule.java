/*
 * Copyright (c) 2022 Unic AG.
 */
package com.unic.commercegradleplugin.codequality.rule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Rule;
import org.gradle.api.Task;
import org.gradle.api.provider.Property;


public class JacocoInstrumentationRule implements Rule {

	private static final String PREFIX = "jacoco";
	private final Project project;
	private final Property<File> jacocoJar;
	private File hybrisDir;

	public JacocoInstrumentationRule(Project project, File hybrisDir) {
		this.hybrisDir = hybrisDir;
		this.project = project;
		this.jacocoJar = project.getObjects().property(File.class);
	}

	@Override
	public String getDescription() {
		return "Pattern: jacoco<target>: Wrap Jacoco instrumentation tasks around <target>";
	}

	@Override
	public void apply(String taskName) {
		if (!taskName.startsWith(PREFIX)) {
			return;
		}
		Task targetTask = project.getTasks().named(dePrefix(taskName)).get();
		Task jacocoTask = project.getTasks().create(taskName, t -> {
			t.doLast(l -> {
				enableJacoco(targetTask.getName());
			});
		});
		Task cleanupTask = project.getTasks().create(taskName + "Cleanup", t -> {
			t.doLast(l -> {
				disableJacoco();
			});
		});
		jacocoTask.finalizedBy(targetTask);
		targetTask.finalizedBy(cleanupTask);
	}


	void enableJacoco(String classifier) {
		project.getLogger().info("BEWARE: Appending Jacoco configuration for {} tests to 'standalone.javaoptions'!", classifier);

		String args = determineJacocoArgs(classifier);

		Path configFile = getConfigFile();
		String newConfig;

		try {
			String config = Files.readString(configFile);
			if (config.matches("(?ms).*^standalone\\.javaoptions=.*?$.*")) {
				newConfig = config.replaceAll("(?m)^(standalone\\.javaoptions=.*?)(-DjacocoConf=start.*-DjacocoConf=end)?$",
						"$1 " + args);
			}
			else {
				newConfig = config + '\n' + "standalone.javaoptions=-DjacocoConf=fulldelete " + args;
			}

			Files.writeString(configFile, newConfig);
		}
		catch (IOException e) {
			throw new GradleException("Failed to enable Jacoco instrumentation!", e);
		}
	}

	String determineJacocoArgs(String classifier) {
		Path destination = hybrisDir.toPath().resolve("log/jacoco/jacoco-" + classifier + ".exec");
		project.getLogger().debug("Jacoco report output file: {}", destination.toAbsolutePath());
		String args = String.format("-DjacocoConf=start -javaagent:%s=destfile=%s,append=true,excludes=*Test -DjacocoConf=end",
				jacocoJar.get(), destination.toAbsolutePath());
		return args;
	}

	void disableJacoco() {
		project.getLogger().info("BEWARE: Removing Jacoco configuration from 'standalone.javaoptions'");

		Path configFile = getConfigFile();

		try {
			String config = Files.readString(configFile);

			config = config.replaceAll("(?m)^(standalone\\.javaoptions=.*?)(.-DjacocoConf=start.*-DjacocoConf=end)?$", "$1");
			config = config.replaceAll("(?m)^\nstandalone\\.javaoptions=-DjacocoConf=fulldelete.*$", "");

			Files.writeString(configFile, config);
		}
		catch (IOException e) {
			throw new GradleException("Failed to disable Jacoco instrumentation!", e);
		}
	}

	Path getConfigFile() {
		return hybrisDir.toPath().resolve("config/local.properties");
	}

	String dePrefix(String input) {
		String dePrefixedInput = input.replaceAll(PREFIX, "");
		return Character.toLowerCase(dePrefixedInput.charAt(0)) + (dePrefixedInput.length() > 1
				? dePrefixedInput.substring(1)
				: "");
	}
}
