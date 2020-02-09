package de.clashsoft.gradle.angular

import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.TaskAction

class BuildAngularTask extends Exec {
	@TaskAction
	@Override
	protected void exec() {
		println this.commandLine.join(' ')
		super.exec()
	}
}
