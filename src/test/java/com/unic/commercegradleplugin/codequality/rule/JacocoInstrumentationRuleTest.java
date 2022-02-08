/*
 * Copyright (c) 2022 Unic AG.
 */

package com.unic.commercegradleplugin.codequality.rule;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;


@RunWith(MockitoJUnitRunner.class)
public class JacocoInstrumentationRuleTest extends TestCase {

	@Mock
	private Project project;

	@Mock
	private ObjectFactory objectFactory;

	@Mock
	private Property<File> jacocoJarProperty;

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private JacocoInstrumentationRule ruleUnderTest;

	private File localproperties;
	private final static String defaultOptionsFormatString = "-DjacocoConf=start -javaagent:null=destfile=%s/log/jacoco/jacoco-%s.exec,append=true,excludes=*Test -DjacocoConf=end";



	@Before
	public void before() throws IOException {
		when(project.getObjects()).thenReturn(objectFactory);
		when(objectFactory.property(File.class)).thenReturn(jacocoJarProperty);
		when(project.getLogger()).thenReturn((Logger) LoggerFactory.getLogger("project"));

		File configFolder = folder.newFolder("config");
		localproperties = configFolder.toPath().resolve("local.properties").toFile();
		localproperties.createNewFile();

		ruleUnderTest = new JacocoInstrumentationRule(project, folder.getRoot());
	}

	@Test
	public void testCleanConfigFile() throws IOException {
		ruleUnderTest.enableJacoco("foobar");

		String actual = Files.readString(localproperties.toPath());
		String expected = String.format("\nstandalone.javaoptions=-DjacocoConf=fulldelete " + defaultOptionsFormatString,
				folder.getRoot().toPath(), "foobar");

		assertEquals(expected, actual);
	}

	@Test
	public void testNoJavaoptionsExist() throws IOException {
		Files.writeString(localproperties.toPath(), "foo=bar\nboo=yakka");

		ruleUnderTest.enableJacoco("foobar");

		String actual = Files.readString(localproperties.toPath());
		String expected = String.format("foo=bar\nboo=yakka\nstandalone.javaoptions=-DjacocoConf=fulldelete " + defaultOptionsFormatString,
				folder.getRoot().toPath(), "foobar");

		assertEquals(expected, actual);
	}

	@Test
	public void testPreexistingJavaoptionsProperty() throws IOException {
		Files.writeString(localproperties.toPath(), "foo=bar\nstandalone.javaoptions=-Xmx2g -Xms128m\nboo=yakka");
		ruleUnderTest.enableJacoco("foobar");

		String actual = Files.readString(localproperties.toPath());
		String expected = String.format(
				"foo=bar\nstandalone.javaoptions=-Xmx2g -Xms128m " + defaultOptionsFormatString + "\nboo=yakka",
				folder.getRoot().toPath(), "foobar");

		assertEquals(expected, actual);
	}

	@Test
	public void testDisableCleanConfigFile() throws IOException {
		ruleUnderTest.disableJacoco();

		String actual = Files.readString(localproperties.toPath());

		assertEquals("", actual);
	}


	@Test
	public void testDisableNoExistingJavaoptionsProperty() throws IOException {
		String expected = "foo=bar\nboo=yakka";
		Files.writeString(localproperties.toPath(), expected);

		ruleUnderTest.disableJacoco();

		String actual = Files.readString(localproperties.toPath());

		assertEquals(expected, actual);
	}

	@Test
	public void testDisablePreexistingJavaoptionsPropertyNoJacoco() throws IOException {
		String expected = "foo=bar\nstandalone.javaoptions=-Xmx2g -Xms128m\nboo=yakka";
		Files.writeString(localproperties.toPath(), expected);

		ruleUnderTest.disableJacoco();

		String actual = Files.readString(localproperties.toPath());

		assertEquals(expected, actual);
	}

	@Test
	public void testDisablePreexistingJavaoptionsProperty() throws IOException {
		Files.writeString(localproperties.toPath(), "foo=bar\nstandalone.javaoptions=-Xmx2g -Xms128m -DjacocoConf=start PLEASEKILLME! -DjacocoConf=end\nboo=yakka");

		ruleUnderTest.disableJacoco();

		String actual = Files.readString(localproperties.toPath());
		String expected = "foo=bar\nstandalone.javaoptions=-Xmx2g -Xms128m\nboo=yakka";

		assertEquals(expected, actual);
	}

	@Test
	public void testDisableFulldelete() throws IOException {
		Files.writeString(localproperties.toPath(), "foo=bar\nboo=yakka\n\nstandalone.javaoptions=-DjacocoConf=fulldelete -Xmx2g -Xms128m PLEASEKILLME!");

		ruleUnderTest.disableJacoco();

		String actual = Files.readString(localproperties.toPath());
		String expected = "foo=bar\nboo=yakka\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testNoNewlineSpam() throws IOException {
		Files.writeString(localproperties.toPath(), "foo=bar\nboo=yakka\n");

		ruleUnderTest.enableJacoco("foobar");
		ruleUnderTest.disableJacoco();
		ruleUnderTest.enableJacoco("foobar");
		ruleUnderTest.disableJacoco();
		ruleUnderTest.enableJacoco("foobar");
		ruleUnderTest.disableJacoco();

		String actual = Files.readString(localproperties.toPath());
		String expected = "foo=bar\nboo=yakka\n";

		assertEquals(expected, actual);
	}

}

