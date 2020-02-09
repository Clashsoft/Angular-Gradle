package de.clashsoft.gradle.angular;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AngularGradlePluginTest
{
	private Project project;

	@Before
	public void setup() {
		// Create a test project and apply the plugin
		this.project = ProjectBuilder.builder().build();
		this.project.getPlugins().apply("java");
		this.project.getPlugins().apply("de.clashsoft.angular-gradle");
	}

	@Test
	public void pluginRegistersInstallAngularDependenciesTask()
	{
		assertNotNull(this.project.getTasks().findByName("installAngularDependencies"));
	}

	@Test
	public void pluginRegistersBuildAngularTask()
	{
		assertNotNull(project.getTasks().findByName("buildAngular"));
	}

	@Test
	public void pluginRegistersCleanAngularTask()
	{
		assertNotNull(project.getTasks().findByName("cleanAngular"));
	}
}
