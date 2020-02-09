package de.clashsoft.gradle.angular

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

import javax.inject.Inject

class AngularGradleConfig {
	final Property<String> appDir
	final Property<String> outputDir

	final Property<String> packageManager
	final ListProperty<String> packageManagerArgs

	@Inject
	AngularGradleConfig(ObjectFactory factory) {
		this.appDir = factory.property(String)
		this.outputDir = factory.property(String)
		this.packageManager = factory.property(String)
		this.packageManagerArgs = factory.listProperty(String)

		this.outputDir.convention(this.appDir.map {
			"$it/dist/angular-gradle-demo"
		})
	}
}
