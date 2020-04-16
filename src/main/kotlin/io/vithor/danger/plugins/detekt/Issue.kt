package io.vithor.danger.plugins.detekt

data class Issue(
    val line: Int,
    val column: Int,
    val severity: SeverityLevel,
    val message: String,
    val sourceRule: String
)