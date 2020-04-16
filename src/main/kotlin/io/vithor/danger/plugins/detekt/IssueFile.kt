package io.vithor.danger.plugins.detekt

data class IssueFile(
    val filePath: String,
    val errors: List<Issue>
)