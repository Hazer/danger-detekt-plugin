package io.vithor.danger.plugins.detekt

import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

internal object DetektParser {

    fun parse(filePath: String): Issues {

        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()

        val document = builder.parse(File(filePath))
        val rootElement = document.documentElement

        val elements = rootElement.getElementsByTagName("file")

        val files = elements.mapElements {
            IssueFile(
                filePath = it.getAttribute("name")
                    .removePrefix(File("").absolutePath)
                    .removePrefix(File.separator),
                errors = it.parseIssues()
            )
        }.distinct()

        return Issues(files)
    }

    private fun Element.parseIssues(): List<Issue> {
        return getElementsByTagName("error").mapElements {
            Issue(
                line = it.getAttribute("line").toIntOrNull() ?: 1,
                column = it.getAttribute("column").toIntOrNull() ?: 1,
                severity = when (it.getAttribute("severity")) {
                    "info" -> SeverityLevel.Info
                    "error" -> SeverityLevel.Error
                    "warn", "warning" -> SeverityLevel.Warning
                    else -> SeverityLevel.Unknown(it.getAttribute("severity"))
                },
                message = it.getAttribute("message"),
                sourceRule = it.getAttribute("source")
            )
        }
    }
}

