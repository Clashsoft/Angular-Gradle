package de.clashsoft.gradle.angular

import groovy.transform.CompileStatic
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@CompileStatic
class AngularGradlePluginFunctionalTest {
	@Rule
	public TemporaryFolder testProjectDir = new TemporaryFolder()

	BuildResult run(GradleRunner runner) {
		println "-" * 30 + " Gradle Output " + "-" * 30
		final BuildResult result = runner.withPluginClasspath().forwardOutput().build()
		println "-" * 30 + " End of Gradle Output " + "-" * 30
		return result
	}

	void downloadAngularGradleDemo() {
		final URL url = new URL('https://github.com/Clashsoft/Angular-Gradle-Demo/archive/master.zip')
		new ZipInputStream(url.openStream()).withStream {
			ZipEntry entry;
			while (entry = it.nextEntry) {
				if (entry.directory) {
					continue
				}

				final File file = new File(testProjectDir.root, entry.name)
				file.parentFile.mkdirs()
				file << it
			}
		}
	}

	@Test
	void build() {
		downloadAngularGradleDemo()

		final File projectDir = new File(testProjectDir.root, 'Angular-Gradle-Demo-master')
		final GradleRunner runner = GradleRunner.create()
			.withArguments('check')
			.withProjectDir(projectDir)
			.withEnvironment(System.getenv())
		final BuildResult result = run(runner)
		Assert.assertNotEquals(TaskOutcome.FAILED, result.task(':check').outcome)
	}
}
