package de.clashsoft.gradle.angular

import org.gradle.api.Plugin
import org.gradle.api.Project

class AngularGradlePlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		final AngularGradleConfig config = project.extensions.create('angular', AngularGradleConfig)
	}
}
