package io.vithor.danger.plugins.detekt

import systems.danger.kotlin.sdk.DangerPlugin
import java.io.File

object Detekt : DangerPlugin() {
    override val id: String
        get() = this.javaClass.name

    fun report(configBlock: Config.() -> Unit) {
        val config = Config().apply(configBlock)
        val reportsDir = config.path?.let { File(it) }

        val filesInFolder = reportsDir?.listFiles { _, name ->
            name.endsWith(".xml")
        }

        val issues = filesInFolder?.map { DetektParser.parse(it.path) } ?: return

        issues.forEach { issue ->
            issue.files.forEach { file ->
                file.errors.forEach {
                    val message = "${it.message} [rule: ${it.sourceRule}]"
                    when (val severity = it.severity) {
                        SeverityLevel.Warning -> context.warn(message, file.filePath, it.line)
                        SeverityLevel.Error -> context.fail(message, file.filePath, it.line)
                        SeverityLevel.Info -> context.message("INFO: $message", file.filePath, it.line)
                        is SeverityLevel.Unknown -> context.message(
                            "${severity.raw}: $message",
                            file.filePath,
                            it.line
                        )
                    }
                }
            }
        }
    }

    class Config {
        var path: String? = null
        var rulesFilter: ((rule: String) -> Boolean)? = null
    }
}

