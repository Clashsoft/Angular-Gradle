package de.clashsoft.gradle.angular

import groovy.transform.Memoized
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.SourceSetContainer

class AngularGradlePlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.pluginManager.apply(JavaPlugin)

		final AngularGradleConfig config = project.extensions.create('angular', AngularGradleConfig)

		// conventions
		config.packageManager.convention(config.appDir.map {
			readNgConfigPackageManager(project.file(it))
		})

		final String buildConfigArg = project.hasProperty('angular-dev') ? '--configuration=gradle' : '--prod'
		config.buildArgs.convention([ 'build', buildConfigArg ])
		config.packageManagerArgs.convention([ 'install' ])

		config.appDir.convention('src/main/angular')
		config.outputDir.convention(config.appDir.map {
			"$it/dist/$project.name"
		})

		// tasks
		project.tasks.register('installAngularDependencies', InstallAngularDependenciesTask) {
			it.group = BasePlugin.BUILD_GROUP
			it.workingDir = config.appDir

			// up-to-date checks cause problems because node_modules is so huge :(
			// just hope the package manager figures out when it does not need to do anything
			// it.inputs.file "$appDir/package.json"
			// it.outputs.files "$appDir/package-lock.json", "$appDir/yarn.lock", "$appDir/pnpm-lock.yaml" // lock files
			// it.outputs.dir "$appDir/node_modules"

			it.executable = mkCmd(config.packageManager.get())
			it.args(config.packageManagerArgs.get())
		}

		project.tasks.register('buildAngular', BuildAngularTask) {
			it.group = BasePlugin.BUILD_GROUP
			it.dependsOn 'installAngularDependencies'

			it.workingDir = config.appDir

			it.executable = mkCmd('ng')
			it.args(config.buildArgs.get())

			it.inputs.files(project.fileTree(config.appDir).exclude('dist', 'node_modules'))
			it.outputs.dir(config.appDir.map { "$it/dist" })
		}

		project.tasks.register('cleanAngular', Delete) {
			it.delete "$config.appDir/dist"
		}

		// setup
		if (!project.hasProperty('no-angular')) {
			final JavaPluginConvention javaPlugin = project.convention.getPlugin(JavaPluginConvention)
			final SourceSetContainer sourceSets = javaPlugin.sourceSets

			sourceSets.main.resources.srcDir(project.files(config.outputDir).builtBy('buildAngular'))
			project.tasks.named('clean') {
				it.dependsOn 'cleanAngular'
			}
		}
	}

	private static String mkCmd(String executable) {
		return isWindows() ? executable + '.cmd' : executable
	}

	private static boolean isWindows() {
		return System.getProperty('os.name').toUpperCase().contains('WINDOWS')
	}

	@Memoized
	private static String readNgConfigPackageManager(File appDir) {
		def cmd = mkCmd('ng')
		def result = execNullable(cmd + ' config cli.packageManager', appDir)
			?: execNullable(cmd + ' config -g cli.packageManager', appDir)
			?: 'npm'
		return result.trim()
	}

	private static String execNullable(String cmd, File dir) {
		final Process process = cmd.execute(null, dir)
		process.waitFor()
		return process.exitValue() == 0 ? process.text : null
	}
}
