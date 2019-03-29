package org.ice1000.tt.project.ui

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import org.ice1000.tt.TTBundle
import org.ice1000.tt.project.*
import org.ice1000.tt.versionOf

abstract class VersionedExecutableProjectConfigurableImpl : VersionedExecutableProjectConfigurable() {
	protected abstract val settings: VersionedExecutableSettings
	protected abstract fun trimVersion(version: String): String

	private fun reinit() {
		versionLabel.text = trimVersion(versionOf(exePathField.text))
	}

	init {
		exePathField.addPropertyChangeListener { reinit() }
	}

	override fun isModified() = exePathField.text != settings.exePath || versionLabel.text != settings.version
	override fun createComponent() = mainPanel
	override fun apply() {
		settings.exePath = exePathField.text
		settings.version = versionLabel.text
	}

	protected fun init() {
		exePathField.text = settings.exePath
		versionLabel.text = settings.version
	}
}

class MiniTTProjectConfigurable(project: Project) : VersionedExecutableProjectConfigurableImpl() {
	// For building searchable options
	override val settings: MiniTTSettings = project.minittSettingsNullable?.settings ?: MiniTTSettings()
	override fun trimVersion(version: String) = version.removePrefix("minittc").trim()

	init {
		init()
		exePathField.addBrowseFolderListener(TTBundle.message("minitt.ui.project.select-compiler"),
			TTBundle.message("minitt.ui.project.select-compiler.description"),
			project,
			FileChooserDescriptorFactory.createSingleFileOrExecutableAppDescriptor())
		guessExeButton.addActionListener {
			minittPath?.let { exePathField.text = it }
		}
	}

	override fun getDisplayName() = TTBundle.message("minitt.name")
}

class AgdaProjectConfigurable(project: Project) : VersionedExecutableProjectConfigurableImpl() {
	// For building searchable options
	override val settings: AgdaSettings = project.agdaSettingsNullable?.settings ?: AgdaSettings()
	override fun trimVersion(version: String) = version.removePrefix("Agda version").trim()

	init {
		init()
		exePathField.addBrowseFolderListener(TTBundle.message("agda.ui.project.select-compiler"),
			TTBundle.message("agda.ui.project.select-compiler.description"),
			project,
			FileChooserDescriptorFactory.createSingleFileOrExecutableAppDescriptor())
		guessExeButton.addActionListener {
			agdaPath?.let { exePathField.text = it }
		}
	}

	override fun getDisplayName() = TTBundle.message("agda.name")
}
